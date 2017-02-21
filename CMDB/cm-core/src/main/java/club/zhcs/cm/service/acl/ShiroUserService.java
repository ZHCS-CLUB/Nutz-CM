package club.zhcs.cm.service.acl;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.google.common.collect.Lists;

import club.zhcs.cm.domain.acl.Permission;
import club.zhcs.cm.domain.acl.Role;
import club.zhcs.cm.domain.acl.User;
import club.zhcs.cm.domain.log.LoginLog;
import club.zhcs.cm.service.log.LoginLogService;
import club.zhcs.titans.utils.common.DateUtils;
import club.zhcs.titans.utils.db.Result;

/**
 * 
 * @author kerbores(kerbores@gmail.com)
 *
 */
@IocBean
public class ShiroUserService {
	Log log = Logs.get();
	@Inject
	UserService userService;

	@Inject
	RoleService roleService;

	@Inject
	PermissionService permissionService;

	@Inject
	LoginLogService loginLogService;

	@Inject
	PropertiesProxy config;

	/**
	 * 检查权限
	 * 
	 * @param permission
	 *            权限名称
	 * @param id
	 *            用户 id
	 * @return 用户是否有参数权限的标识
	 *
	 * @author 王贵源
	 */
	public boolean checkPermission(String permission, int id) {

		for (String p : getAllPermissionsInfo(id)) {
			if (Strings.equals(p, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查角色
	 * 
	 * @param role
	 *            角色名称
	 * @param id
	 *            用户 id
	 * @return 用户是否有参数角色的标识
	 *
	 * @author 王贵源
	 */
	public boolean checkRole(String role, int id) {
		for (String r : getRolesInfo(id)) {
			if (Strings.equals(role, r)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据用户名查询用户
	 * 
	 * @param userName
	 *            用户名
	 * @return 用户
	 *
	 * @author 王贵源
	 */
	public User findByName(String userName) {
		return userService.fetch(userName);
	}

	/**
	 * 查询用户的全部权限
	 * 
	 * @param id
	 *            用户 id
	 * @return 权限列表
	 *
	 * @author 王贵源
	 */
	public List<Permission> getAllPermissions(int id) {
		return permissionService.getAllPermissionsByUserId(id);
	}

	/**
	 * 根据用户获取权限信息
	 * 
	 * @param id
	 *            用户 id
	 * @return 权限名称列表
	 *
	 * @author 王贵源
	 */
	public List<String> getAllPermissionsInfo(int id) {
		List<Permission> permissions = getAllPermissions(id);
		final List<String> target = Lists.newArrayList();
		Lang.each(permissions, new Each<Permission>() {

			@Override
			public void invoke(int index, Permission ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				target.add(ele.getName());
			}
		});
		return target;
	}

	/**
	 * 获取用户全部角色
	 * 
	 * @param id
	 *            用户 id
	 * @return 角色列表
	 *
	 * @author 王贵源
	 */
	public List<Role> getAllRoles(int id) {
		// XXX 直接权限即全部权限
		return getDirectRoles(id);
	}

	/**
	 * 获取用户直接角色
	 * 
	 * @param id
	 *            用户 id
	 * @return 角色列表
	 *
	 * @author 王贵源
	 */
	public List<Role> getDirectRoles(int id) {
		return roleService.listByUserId(id);
	}

	/**
	 * 获取用户间接角色
	 * 
	 * @param id
	 *            用户 id
	 * @param type
	 *            用户类型
	 * @return 角色列表
	 *
	 * @author 王贵源
	 */
	public List<Role> getIndirectRoles(int id) {
		return Lists.newArrayList();
	}

	/**
	 * 获取用户的菜单权限
	 * 
	 * @param id
	 *            用户 id
	 * @return 权限列表
	 *
	 * @author 王贵源
	 */
	public List<Permission> getMenuPermissions(int id) {
		return getAllPermissions(id);
	}

	/**
	 * 获取用户的角色信息列表
	 * 
	 * @param id
	 *            用户 id
	 * @return 角色名称列表
	 *
	 * @author 王贵源
	 */
	public List<String> getRolesInfo(int id) {
		final List<String> roles = Lists.newArrayList();
		Lang.each(getAllRoles(id), new Each<Role>() {

			@Override
			public void invoke(int index, Role ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				roles.add(ele.getName());
			}
		});
		return roles;
	}

	/**
	 * 用户登录
	 *
	 * @param userName
	 *            用户名
	 * @param p
	 *            密码
	 * @return 登录结果
	 *
	 * @author 王贵源
	 */
	public Result login(String userName, String p, String ip) {
		int times = config.getInt("login.try.times", 5);
		LoginLog loginLog = new LoginLog();
		loginLog.setAccount(userName);
		loginLog.setIp(ip);
		try {
			if (loginLogService.count(Cnd.where("account", "=", userName).and("success", "=", false).and("loginTime",
					"between", new Date[] { DateUtils.getDayStart(), DateUtils.getDayEnd() })) > times) {
				userService.update(Chain.make("delete", true), Cnd.where("name", "=", userName));// 锁定用户和
				loginLog.setSuccess(false);
				return Result.fail(String.format("当日登录失败次数已达到 %d 次,请明日再试,或联系系统管理员", times));
			}
			User user = findByName(userName);
			if (user == null) {
				loginLog.setSuccess(false);
				return Result.fail("用户名或密码不存在");
			}
			if (!user.isAvailable()) {
				loginLog.setSuccess(false);
				return Result.fail("账户被锁定");
			}
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(userName, p);
			token.setRememberMe(true);
			currentUser.login(token);
			loginLog.setUserId(user.getId());
			loginLog.setSuccess(true);
			return Result.success().addData("loginUser", user);
		} catch (LockedAccountException e) {
			loginLog.setSuccess(false);
			log.debug(e);
			return Result.fail("账户被锁定");
		} catch (Exception e) {
			loginLog.setSuccess(false);
			log.debug(e);
			return Result.fail("登录失败");
		} finally {
			loginLogService.save(loginLog);
		}
	}

}
