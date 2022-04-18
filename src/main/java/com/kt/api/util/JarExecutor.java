package com.kt.api.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JarExecutor extends Thread {

    String jarPath = "abcd.jar";
    String parameter = "";
    @Override
    public void run(){
    if (!StringUtils.isEmpty(jarPath)) {
        File jar = new File(jarPath);
        if (jar.exists()) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(jar.getParentFile());
            List<String> commands = new ArrayList<>();
            commands.add("java");

            commands.add("-jar");
            commands.add(jarPath);
            if (!StringUtils.isEmpty(parameter)) commands.add(parameter);
            processBuilder.command(commands);
            log.info("Running Job details as follows >>>>>>>>>>>>>>>>>>>>: ");
            StringBuilder sb = new StringBuilder();
            commands.forEach((s) -> sb.append(s));
            log.info("Running Job commands : " + sb.toString());
            try {
                Process p = processBuilder.start();
                OutputHandler out
                        = new OutputHandler(p.getInputStream(), "euc-kr");
                OutputHandler err
                        = new OutputHandler(p.getErrorStream(), "euc-kr");

                // 아래는 pid 저장 로직
                // pid 사용하려면 java 11을 사용해야 함.
                //pid = p.pid();
                //startJobRun.setPid(pid);
                //schedulerService.saveRun(startJobRun);
                int status = p.waitFor();
                out.join();
                err.join();
                if (status != 0) {
                    System.out.println("Error:");
                    System.out.println(err.getText());
                    //아래는 fail message 저장 로직
                    //startJobRun.setJobRunState(JobRunStateEnum.FAILED.name());
                    //schedulerService.saveRun(startJobRun);
                }
            } catch (Exception e) {
                //아래는 fail message 저장 로직
                //startJobRun.setJobRunState(JobRunStateEnum.FAILED.name());
                //schedulerService.saveRun(startJobRun);
            }
        } else try {
            throw new Exception("Job Jar not found :  " + jarPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
}
