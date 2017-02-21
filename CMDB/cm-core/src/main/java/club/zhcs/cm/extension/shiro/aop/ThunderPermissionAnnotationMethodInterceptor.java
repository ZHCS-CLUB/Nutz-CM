package club.zhcs.cm.extension.shiro.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;

/**
 * 
 * @author kerbores(kerbores@gmail.com)
 *
 */
public class ThunderPermissionAnnotationMethodInterceptor extends PermissionAnnotationMethodInterceptor {

	/**
	 * 
	 */
	public ThunderPermissionAnnotationMethodInterceptor() {
		setHandler(new ThunderPermissionAnnotationHandler());
	}

	/**
	 * 
	 * @param resolver
	 */
	public ThunderPermissionAnnotationMethodInterceptor(AnnotationResolver resolver) {
		setHandler(new ThunderPermissionAnnotationHandler());
		setResolver(resolver);
	}
}
