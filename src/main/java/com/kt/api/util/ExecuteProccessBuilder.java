package com.kt.api.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j

public class ExecuteProccessBuilder  extends Thread {

    Long pid=0L;
    @Override
    public void run(){

        String jobType="";
        ProcessBuilder pb=null;
        //트리거는 여러 사용자의 job을 조합해서 만들수 있으므로 job의 경로를 사용한다.
        //TODO 여기에 DEV 대신에 공용 저장소로 변경해 준다.
        String scriptLocation="C:/DEV/test.py";
        boolean isExists=fileExistsCheck(scriptLocation);
        try {
        if(!isExists){
            log.error(" 스크립트 경로 : " + scriptLocation +" : File Not Found");
            throw new Exception("File Not Found!");
        }

            switch (jobType) {
                case "PYTHON":
                    pb = new ProcessBuilder("python", scriptLocation);
                    break;
                case "R":
                    pb = new ProcessBuilder("R", scriptLocation);
                    break;
                case "MATLAB":
                    pb = new ProcessBuilder("matlab", scriptLocation);
                    break;
                default:
                    log.error("Invalid Script Type");
                    break;
            }
            Process p = pb.start();
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
                return;
            }
        } catch (Exception e) {
            //아래는 fail message 저장 로직
            //startJobRun.setJobRunState(JobRunStateEnum.FAILED.name());
            //schedulerService.saveRun(startJobRun);
        }
    }

    public boolean fileExistsCheck(String location) {
        File f = new File(location);
        if (!f.exists()) {
            return false;
        }
        return true;
    }
}
