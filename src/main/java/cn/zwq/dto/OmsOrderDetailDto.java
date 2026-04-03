package cn.zwq.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString

public class OmsOrderDetailDto extends OmsOrderDto{

    @Schema(description = "促销立减金额")
    private BigDecimal discountPrice;

    @Schema(description = "客户id")
    private Long customerId;

    @Schema(description = "昵称")
    private String nikeName;

    @Schema(description = "注册手机号 脱敏")
    private String regPhone;

    @Schema(description = "加密后注册手机号")
    private String encryptRegPhone;

    @Schema(description = "实名手机号 脱敏")
    private String realPhone;

    @Schema(description = "加密后实名手机号")
    private String encryptRealPhone;

    @Schema(description = "支付状态 1-待支付 2-已支付 3-已退款 4-已取消 5-支付失败 6-已抵扣新订单 7-部分退款")
    private Integer payStatus;

    @Schema(description = "实名")
    private String realName;

    @Schema(description = "原始归属人")
    private String empName;

    @Schema(description = "身份证号码 脱敏")
    private String idCard;

    @Schema(description = "证件类型：0-身份证,1-护照,B-港澳居民来往内地通行证,C-台湾居民来往大陆通行证")
    private String certType;

    @Schema(description = "加密的身份证号码")
    private String encryptIdCard;

    @Schema(description = "风险测评类型")
    private String riskResult;

    @Schema(description = "wxUnionId")
    private String wxUnionId;

    @Schema(description = "wxOpenId")
    private String wxOpenId;

    @Schema(description = "合同编号")
    private String contractNumber;

    @Schema(description = "签署时间")
    private LocalDateTime signingTime;

    @Schema(description = "下单时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "性别 0-无 1-男 2-女")
    private Integer gender;

    @Schema(description = "微信头像")
    private String wxAvatar;

    @Schema(description = "生日")
    private String birth;

    @Schema(description = "风测到期时间")
    private String expireTime;


    @Schema(description = "服务到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expirationTime;

    @Schema(description = "权益状态:1.待生效，2.生效中，3.退款中已暂停，4.退款完成已失效，5.升单完成已失效，6.过期已失效")
    private Integer equityStatus;

    @Schema(description = "是否可以退款 0-不可退款 1-可退款")
    private Integer canRefund;

    @Schema(description = "客户经理")
    private String customerService;

    @Schema(description = "合同类型")
    private String contractType;

    @Schema(description = "问卷回访状态 0-待回访 1-回访通过 2-回访拒绝 3-免回访")
    private Integer returnVisitStatus;

    @Schema(description = "问卷回访链接")
    private String returnVisitUrl;

    @Schema(description = "人工回访状态 0-待回访 1-回访通过 2-回访拒绝 3-免回访")
    private Integer manualReturnVisitStatus;

    @Schema(description = "服务开通状态：0-待开通 1-已通过 2-拒绝开通")
    private Integer approveStatus;

    @Schema(description = "辅转主状态: 0-未执行 1-已执行")
    private Integer toMainStatus;

    @Schema(description = "客户关联微信idList")
    private List<String> externalUserIdList;

    @Schema(description = "介绍人id")
    private String referrerExternalUserId;

    @Schema(description = "介绍人微信昵称")
    private String referrerWxNickname;

    @Schema(description = "介绍人微信头像")
    private String referrerWxAvatar;

    @Schema(description = "客户openId")
    private String cusOpenId;

    @Schema(description = "用户收货信息状态 1未填写 2已填写 3、已清除")
    private Integer userReceivingInfoState;

    @Schema(description = "再次退款按钮是否展示")
    private Boolean refundAgainButton;

    @Schema(description = "签约类型按钮是否展示")
    private Boolean showSignTypeButton;

    @Schema(description = "签约类型")
    private Integer signType;
}
