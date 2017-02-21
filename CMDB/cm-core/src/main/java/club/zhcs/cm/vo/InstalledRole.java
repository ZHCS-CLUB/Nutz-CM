package club.zhcs.cm.vo;

/**
 * 
 * @author kerbores(kerbores@gmail.com)
 *
 */
public enum InstalledRole {
	/**
	 * 超级管理员
	 */
	SU("su", "超级管理员"),
	/**
	 * 项目负责人
	 */
	PROJECT_LEADER("project.leader", "项目负责人"),
	/**
	 * 项目成员
	 */
	PROJECT_MEMBER("project.member", "项目成员");
	private String name;

	private String description;

	/**
	 * @param name
	 * @param description
	 */
	private InstalledRole(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
