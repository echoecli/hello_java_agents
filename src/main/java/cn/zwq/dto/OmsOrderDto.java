package cn.zwq.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OmsOrderDto {

    @Schema(description = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "客户id")
    private String cusOpenId;

    @Schema(description = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @Schema(description = "skuId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品等级")
    private Integer productLevel;

    @Schema(description = "商品是否开启高龄回访 0否 1是")
    private Integer productElderlyVisit;

    @Schema(description = "原价")
    private BigDecimal price;

    @Schema(description = "商品售价")
    private BigDecimal salePrice;

    @Schema(description = "优惠券金额")
    private BigDecimal couponAmount;

    @Schema(description = "订单金额")
    private BigDecimal totalAmount;

    @Schema(description = "历史订单抵扣金额")
    private BigDecimal historicalDiscountAmount;

    @Schema(description = "应付金额")
    private BigDecimal payableAmount;

    @Schema(description = "已支付金额")
    private BigDecimal paidAmount;

    @Schema(description = "在途金额")
    private BigDecimal pendingAmount;

    @Schema(description = "到账金额")
    private BigDecimal receivedAmount;

    @Schema(description = "支付时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime paymentTime;

    @Schema(description = "实名认证方式 1-短信 2人脸识别")
    private Integer realNameVerifyWay;

    @Schema(description = "签署合同方式 1-短信 2人脸识别")
    private Integer signVerifyWay;

    @Schema(description = "合同状态")
    private Integer contractStatus;

    @Schema(description = "支付状态 1-待支付 2-已支付 3-已退款 4-已取消 5-支付失败 6-已抵扣新订单")
    private Integer payStatus;

    @Schema(description = "订单来源：0->PC订单；1->app订单")
    private Integer sourceType;

    @Schema(description = "订单类型：1-普通订单 2-升级订单 3-活动订单")
    private Integer orderType;

    @Schema(description = "订单备注")
    private String note;

    @Schema(description = "风测结果id")
    private Long questionResultId;

    @Schema(description = "风险确认书链接")
    private String riskConfirmationLink;

    @Schema(description = "适当性评估书链接")
    private String adequacyLink;

    @Schema(description = "订单有效期：支付时间+商品有效期")
    private LocalDateTime expirationTime;

    @Schema(description = "邀请码(员工id)")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long empId;

    @Schema(description = "员工姓名")
    private String empName;

    @Schema(description = "主任id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long leaderEmpId;

    @Schema(description = "主任姓名")
    private String leaderEmpName;

    @Schema(description = "经理id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long managerEmpId;

    @Schema(description = "经理姓名")
    private String managerEmpName;

    @Schema(description = "部门id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "经理部门id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long managerDeptId;

    @Schema(description = "经理部门名称")
    private String managerDeptName;

    /**
     * 签约类型
     */
    private Integer signType;

    @Schema(description = "员工部门ids")
    private String deptIds;

    @Schema(description = "回访状态 0-待回访 1-回访通过 2-回访拒绝 3-免回访")
    private Integer returnVisitStatus;

    @Schema(description = "问卷回访结果id")
    private Long returnVisitResultId;

    @Schema(description = "人工回访状态 0-待回访 1-回访通过 2-回访拒绝 3-免回访")
    private Integer manualReturnVisitStatus;

    @Schema(description = "服务开通状态：0-待开通 1-已开通 2-拒绝开通")
    private Integer approveStatus;

    @Schema(description = "审核时间")
    private LocalDateTime approveTime;

    @Schema(description = "审核内容")
    private String approveContent;

    @Schema(description = "(小鹅通)订单权益状态: 0-未开通，1-已开通")
    private Integer equityStatus;

    @Schema(description = "源商品id")
    private Long oldProductId;

    @Schema(description = "源订单号列表，升级订单后要关闭的订单集合")
    private String oldOrderSnList;

    @Schema(description = "升级订单的来源订单编号")
    private String sourceOrderSn;

    @Schema(description = "服务过期状态 0-未过期 1-已过期")
    private Integer expireStatus;

    @Schema(description = "订单购买流转状态 0-未流转 1-已流转")
    private Integer transferStatus;

    @Schema(description = "辅转主状态 0-未执行 1-已执行")
    private Integer toMainStatus;

    @Schema(description = "归属人")
    private String ownerId;

    /**
     * 订单类型
     */
    private Integer orderCategory;
}
