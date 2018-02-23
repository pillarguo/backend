package cmdc.bean;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "information")
public class Information {
	
	@PathParam("info_id")
    private Integer informationId;

	@FormParam("informationTitle")
    private String informationTitle;

	@FormParam("informationImage")
    private String informationImage;

	@FormParam("informationSummary")
    private String informationSummary;

	@FormParam("viewTimes")
    private Integer viewTimes;

	@FormParam("cmdcAdminId")
    private Integer cmdcAdminId;

	@FormParam("createTime")
    private Date createTime;

	@FormParam("updateCmdcAdminId")
    private Integer updateCmdcAdminId;

	@FormParam("updateTime")
    private Date updateTime;

	@FormParam("displayStatus")
    private String displayStatus;

	@FormParam("reserveField1")
    private String reserveField1;

	@FormParam("reserveField2")
    private String reserveField2;

	@FormParam("reserveField3")
    private String reserveField3;

	@FormParam("reserveField4")
    private String reserveField4;

	@FormParam("reserveField5")
    private String reserveField5;

	@FormParam("informationContent")
    private String informationContent;

    public Integer getInformationId() {
        return informationId;
    }

    public void setInformationId(Integer informationId) {
        this.informationId = informationId;
    }

    public String getInformationTitle() {
        return informationTitle;
    }

    public void setInformationTitle(String informationTitle) {
        this.informationTitle = informationTitle == null ? null : informationTitle.trim();
    }

    public String getInformationImage() {
        return informationImage;
    }

    public void setInformationImage(String informationImage) {
        this.informationImage = informationImage == null ? null : informationImage.trim();
    }

    public String getInformationSummary() {
        return informationSummary;
    }

    public void setInformationSummary(String informationSummary) {
        this.informationSummary = informationSummary == null ? null : informationSummary.trim();
    }

    public Integer getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Integer viewTimes) {
        this.viewTimes = viewTimes;
    }

    public Integer getCmdcAdminId() {
        return cmdcAdminId;
    }

    public void setCmdcAdminId(Integer cmdcAdminId) {
        this.cmdcAdminId = cmdcAdminId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateCmdcAdminId() {
        return updateCmdcAdminId;
    }

    public void setUpdateCmdcAdminId(Integer updateCmdcAdminId) {
        this.updateCmdcAdminId = updateCmdcAdminId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus == null ? null : displayStatus.trim();
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

    public String getInformationContent() {
        return informationContent;
    }

    public void setInformationContent(String informationContent) {
        this.informationContent = informationContent == null ? null : informationContent.trim();
    }
}
