package pers.hsc.evats.core.common.handler;

import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import com.baomidou.mybatisplus.mapper.MetaObjectHandler;

import pers.hsc.evats.core.common.constant.DataBaseConstant;
import pers.hsc.evats.core.common.constant.DataBaseFieldConstant;
import pers.hsc.evats.modules.sys.utils.UserUtils;

/**
 * @author hsc
 *
 *         Mar 29, 2018
 */
public class BaseMetaObjectHandler extends MetaObjectHandler {

	/**
	 * 新增
	 */
	public void insertFill(MetaObject metaObject) {
		// 创建用户
		Object createBy = getFieldValByName(DataBaseFieldConstant.CREATE_BY, metaObject);
		if (createBy == null) {
			setFieldValByName(DataBaseFieldConstant.CREATE_BY, UserUtils.getUser(), metaObject);
		}

		// 创建时间
		Object createDate = getFieldValByName(DataBaseFieldConstant.CREATE_DATE, metaObject);
		if (createDate == null) {
			setFieldValByName(DataBaseFieldConstant.CREATE_DATE, new Date(), metaObject);
		}

		// 删除标记
		Object delFlag = getFieldValByName(DataBaseFieldConstant.DEL_FLAG, metaObject);
		if (delFlag == null) {
			setFieldValByName(DataBaseFieldConstant.DEL_FLAG, DataBaseConstant.DEL_FLAG_NORMAL, metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 更新用户
		Object updateBy = getFieldValByName(DataBaseFieldConstant.UPDATE_BY, metaObject);
		if (updateBy == null) {
			setFieldValByName(DataBaseFieldConstant.UPDATE_BY, UserUtils.getUser(), metaObject);
		}

		// 更新用户
		Object updateDate = getFieldValByName(DataBaseFieldConstant.UPDATE_DATE, metaObject);
		if (updateDate == null) {
			setFieldValByName(DataBaseFieldConstant.UPDATE_DATE, new Date(), metaObject);
		}
	}
}