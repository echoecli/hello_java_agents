package cn.zwq.dto;

import lombok.Data;

/**
 * 心理健康风险等级Dto
 */
@Data
public class MentalHealthRiskDto {

    /**
     * 心理健康风险等级
     */
    private String riskLevel;

    /**
     * 建议
     */
    private String suggest;

    /**
     * 城市
     */
    private String city;

}
