package com.ryuntech.common.constant.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author EDZ
 */

@Getter
public enum ExceptionEnum {
    INVALID_FILE_TYPE(400, "无效的文件类型！"),
    INVALID_PARAM_ERROR(400, "无效的请求参数！"),
    INVALID_PHONE_NUMBER(400, "无效的手机号码"),
    INVALID_VERIFY_CODE(400, "验证码错误！"),
    INVALID_USERNAME_PASSWORD(400, "无效的用户名和密码！"),
    INVALID_SERVER_ID_SECRET(400, "无效的服务id和密钥！"),
    INVALID_NOTIFY_PARAM(400, "回调参数有误！"),
    INVALID_NOTIFY_SIGN(400, "回调签名有误！"),

    APPLICATION_NOT_FOUND(404, "应用不存在！"),
    ORDER_NOT_FOUND(404, "订单不存在！"),
    ORDER_DETAIL_NOT_FOUND(404, "合同数据不存在！"),

    DATA_TRANSFER_ERROR(500, "数据转换异常！"),
    INSERT_OPERATION_FAIL(500, "新增操作失败！"),
    UPDATE_OPERATION_FAIL(500, "更新操作失败！"),
    DELETE_OPERATION_FAIL(500, "删除操作失败！"),

    FILE_UPLOAD_ERROR(500, "文件上传失败！"),
    DIRECTORY_WRITER_ERROR(500, "目录写入失败！"),
    FILE_WRITER_ERROR(500, "文件写入失败！"),
    SEND_MESSAGE_ERROR(500, "短信发送失败！"),
    INVALID_ORDER_STATUS(500, "合同状态不正确！"),


    /**
     * role错误
     */
    ROLE_STATUS(500, "角色id不能为空, 操作失败！"),
    ROLE_NOT_FOUND(500, "角色不存在, 操作失败！"),
    ROLE_NOT_EDIT(500, "管理员角色不允许编辑, 操作失败！"),
    ROLE_NOT_DELETE(500, "管理员角色不允许删除, 操作失败！"),
    ROLE_IS_RELATION(500, "该角色下有关联员工, 请先为员工分配新角色！"),
    USER_NOT_FOUND(500, "系统错误, 无法获取当前操作用户信息！"),
    ROLE_NOT_DIFF(500, "已经存在同名的角色, 请指定新的角色名！"),
    ROLE_ERROR(500, "您所选择的角色权限有误, 请重新为角色指定权限!"),
    DEPARTMENT_IS_FOUND(500, "已经存在同名的部门, 请指定新的部门名!"),
    DEPARTMENT_NOT_FOUND(500, "上级部门不存在, 请指定新的上级部门!"),
    DEPARTMENT_ERROR_1(500, "目前系统最高仅支持 4 级部门, 请重新指定其他的上级部门!"),
    DEPARTMENT_ERROR_2(500, "部门不存在, 操作失败!"),
    DEPARTMENT_ERROR_3(500, "不能选择自己为父级部门!"),
    DEPARTMENT_ERROR_4(500, "至少保留一个一级部门!"),
    DEPARTMENT_ERROR_5(500, "该部门存在下级部门, 请先删除下级部门!"),
    DEPARTMENT_ERROR_6(500, "该部门存在关联员工, 请先将员工迁移到其他部门!"),
    DEPARTMENT_ERROR_7(500, "移出部门不存在!"),
    DEPARTMENT_ERROR_8(500, "目标部门不存在!"),

    UNAUTHORIZED(401, "登录失效或未登录！");

    private int status;
    private String message;

    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
