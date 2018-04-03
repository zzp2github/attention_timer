package com.hexun.attention.disconf;

import org.springframework.stereotype.Component;

import com.baidu.disconf.client.common.annotations.DisconfFile;

/**
 * 描述：xxl-job-executor配置文件[没有定义变量，只考虑存储位置]
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年6月28日
 * @version v1.0
 */
@Component
@DisconfFile(filename = "xxl-job-executor.properties", targetDirPath = "config/properties")
public class XXLJobExecutorConfig {

}
