package club.zhcs.cm.extension.shiro.aop;

import java.lang.annotation.Annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.subject.Subject;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;

import club.zhcs.cm.extension.shiro.anno.ThunderRequiresPermissions;
import club.zhcs.cm.vo.InstallPermission;

/**
 * 
 * @author kerbores(kerbores@gmail.com)
 *
 */
public class ThunderPermissionAnnotationHandler extends PermissionAnnotationHandler {

	public ThunderPermissionAnnotationHandler() {
		setAnnotationClass(ThunderRequiresPermissions.class);
	}

	@Override
	public void assertAuthorized(Annotation a) throws AuthorizationException {
		if (!(a instanceof ThunderRequiresPermissions))
			return;

		ThunderRequiresPermissions rpAnnotation = (ThunderRequiresPermissions) a;
		InstallPermission[] perms_ = rpAnnotation.value();
		Subject subject = getSubject();

		final String[] perms = new String[perms_.length];

		Lang.each(perms_, new Each<InstallPermission>() {

			@Override
			public void invoke(int index, InstallPermission ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				perms[index] = ele.getName();
			}
		});

		if (perms.length == 1) {
			subject.checkPermission(perms[0]);
			return;
		}
		if (Logical.AND.equals(rpAnnotation.logical())) {
			getSubject().checkPermissions(perms);
			return;
		}
		if (Logical.OR.equals(rpAnnotation.logical())) {
			boolean hasAtLeastOnePermission = false;
			for (String permission : perms)
				if (getSubject().isPermitted(permission))
					hasAtLeastOnePermission = true;
			if (!hasAtLeastOnePermission)
				getSubject().checkPermission(perms[0]);
		}
	}
}
