package balance;

/**
 * @author liuxin
 * 2022/5/18 17:03
 */

/**
 * String ip：负载IP
 * final Integer weight：权重，保存配置的权重
 * Integer effectiveWeight：有效权重，轮询的过程权重可能变化
 * Integer currentWeight：当前权重，比对该值大小获取节点
 * 第一次加权轮询时：currentWeight = weight = effectiveWeight
 * 后面每次加权轮询时：currentWeight 的值都会不断变化，其他权重不变
 */
public class Node implements Comparable<Node> {
    private String ip;
    private final Integer weight;
    private Integer effectiveWeight;
    private Integer currentWeight;

    public Node(String ip, Integer weight) {
        this.ip = ip;
        this.weight = weight;
        this.effectiveWeight = weight;
        this.currentWeight = weight;
    }

    public Node(String ip, Integer weight, Integer effectiveWeight, Integer currentWeight) {
        this.ip = ip;
        this.weight = weight;
        this.effectiveWeight = effectiveWeight;
        this.currentWeight = currentWeight;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getEffectiveWeight() {
        return effectiveWeight;
    }

    public void setEffectiveWeight(Integer effectiveWeight) {
        this.effectiveWeight = effectiveWeight;
    }

    public Integer getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight = currentWeight;
    }

    @Override
    public int compareTo(Node node) {
        return currentWeight > node.currentWeight ? 1 : (currentWeight.equals(node.currentWeight) ? 0 : -1);
    }

    @Override
    public String toString() {
        return "{ip='" + ip + "', weight=" + weight + ", effectiveWeight=" + effectiveWeight + ", currentWeight=" + currentWeight + "}";
    }
}
