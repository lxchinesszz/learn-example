package learn.maven.plugin;

import learn.common.print.ColorConsole;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author lx
 */
@Mojo(name = "versionCheck", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
@Execute(phase = LifecyclePhase.PACKAGE)
public class ArtifactVersionCheckMojo extends AbstractMojo {

    /**
     * 用于接收外部传递的正则表达
     */
    @Parameter(property = "versionCheckRegular")
    private String[] assertDependencyRegular;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Component(hint = "default")
    private DependencyGraphBuilder dependencyGraphBuilder;


    /**
     * requiresDependencyResolution 在插件运行之前就将所有的依赖模块给构建好
     * requiresDependencyCollection 这个注解不会解析依赖项的文件,只分析依赖关系
     *
     * @throws MojoExecutionException 运行异常
     * @throws MojoFailureException   异常
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (assertDependencyRegular != null && assertDependencyRegular.length > 0) {
            ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());
            List<MavenProject> projects = session.getProjects();
            StringBuilder error = new StringBuilder();
            for (MavenProject mavenProject : projects) {
                buildingRequest.setProject(mavenProject);
                DependencyNode rootNode = null;
                try {
                    rootNode = dependencyGraphBuilder.buildDependencyGraph(buildingRequest, new ScopeArtifactFilter("test"));
                } catch (DependencyGraphBuilderException e) {
                    throw new RuntimeException(e);
                }
                checkVersion(rootNode, error);
            }
            // 如果不等于空说明有问题
            if (StringUtils.isNotBlank(error.toString())) {
                getLog().error("version 规则拦截:" + error);
                throw new MojoFailureException("version 规则拦截:" + error);
            }
        }
    }

    /**
     * 如果正则匹配上就返回true
     *
     * @param version 版本信息
     * @return boolean
     */
    private boolean illegalVersion(String version) {
        for (String pattern : assertDependencyRegular) {
            if (Pattern.matches(pattern, version)) {
                return true;
            }
        }
        return false;
    }

    private void checkVersion(DependencyNode node, StringBuilder error) {
        Artifact artifact = node.getArtifact();
        String version = artifact.getVersion();
        if (illegalVersion(version)) {
            error.append(artifact).append("\t\n");
        }
        List<DependencyNode> childrenList = node.getChildren();
        if (CollectionUtils.isNotEmpty(childrenList)) {
            for (DependencyNode dependencyNode : childrenList) {
                checkVersion(dependencyNode, error);
            }
        }
    }

    private void extracted(String title, Set<Artifact> dependencies) {
        for (Artifact dependency : dependencies) {
            String groupId = dependency.getGroupId();
            String artifactId = dependency.getArtifactId();
            String version = dependency.getVersion();
            ColorConsole.colorPrintln("{},groupId:{},artifactId:{},version:{}", title, groupId, artifactId, version);
        }
    }
}
