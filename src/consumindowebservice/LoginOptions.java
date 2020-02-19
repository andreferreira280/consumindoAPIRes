package consumindowebservice;

public class LoginOptions {
	private String session;
	private String userId;
	private String organizationId;
	private String licenseType;
private String urlBase;

	public LoginOptions(String sessionID, String userId, String organizationId, String licenseType) {
		this.session = sessionID;
		this.userId = userId;
		this.organizationId = organizationId;
		this.licenseType = licenseType;
	}

	public LoginOptions() {
	}

	public String getSessionID() {
		return session;
	}

	public void setSessionID(String sessionID) {
		this.session = sessionID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
public String getUrlBase() {
	return urlBase;
}

public void setUrlBase(String urlBase) {
this.urlBase = urlBase;
}
}
