package cmdc.bean;

import java.util.Date;

import javax.inject.Singleton;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "admin")
public class Admin {

	@PathParam("cmdcAdminId")
    private Integer cmdcAdminId;

	@FormParam("cmdcAdminName")
    private String cmdcAdminName;

	@FormParam("phoneNumber")
    private String phoneNumber;

	@FormParam("password")
    private String password;

	@FormParam("realName")
    private String realName;
	
	@FormParam("department")
    private String department;
	
	@FormParam("roleType")
    private String roleType;

	@FormParam("createTime")
    private Date createTime;

	@FormParam("updateTime")
    private Date updateTime;

	@FormParam("loginTime")
    private Date loginTime;

	@FormParam("logoutTime")
    private Date logoutTime;
	
	//是否被注销，1注销，0正常
	@FormParam("roleCancelStatus")
	private String roleCancelStatus;

	@FormParam("reserveField1")
    private String reserveField1;

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRoleCancelStatus() {
		return roleCancelStatus;
	}

	public void setRoleCancelStatus(String roleCancelStatus) {
		this.roleCancelStatus = roleCancelStatus;
	}

	@FormParam("reserveField2")
    private String reserveField2;

	@FormParam("reserveField3")
    private String reserveField3;

	@FormParam("reserveField4")
    private String reserveField4;

	@FormParam("reserveField5")
    private String reserveField5;

    public Integer getCmdcAdminId() {
        return cmdcAdminId;
    }

    public void setCmdcAdminId(Integer cmdcAdminId) {
        this.cmdcAdminId = cmdcAdminId;
    }

    public String getCmdcAdminName() {
        return cmdcAdminName;
    }

    public void setCmdcAdminName(String cmdcAdminName) {
        this.cmdcAdminName = cmdcAdminName == null ? null : cmdcAdminName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType == null ? null : roleType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getReserveField1() {
        return reserveField1;
    }

    public void setReserveField1(String reserveField1) {
        this.reserveField1 = reserveField1 == null ? null : reserveField1.trim();
    }

    public String getReserveField2() {
        return reserveField2;
    }

    public void setReserveField2(String reserveField2) {
        this.reserveField2 = reserveField2 == null ? null : reserveField2.trim();
    }

    public String getReserveField3() {
        return reserveField3;
    }

    public void setReserveField3(String reserveField3) {
        this.reserveField3 = reserveField3 == null ? null : reserveField3.trim();
    }

    public String getReserveField4() {
        return reserveField4;
    }

    public void setReserveField4(String reserveField4) {
        this.reserveField4 = reserveField4 == null ? null : reserveField4.trim();
    }

    public String getReserveField5() {
        return reserveField5;
    }

    public void setReserveField5(String reserveField5) {
        this.reserveField5 = reserveField5 == null ? null : reserveField5.trim();
    }
}
