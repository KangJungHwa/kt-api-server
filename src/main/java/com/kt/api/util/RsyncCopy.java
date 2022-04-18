package com.kt.api.util;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class RsyncCopy extends Thread {
    String userName=null;
    String jobType=null;
    boolean isSuccess=false;
    String[] sourceExtraPyfiles=null;
    String[] sourceExtraFiles=null;

    @Override
    public void run(){
        try {
            String sourceDirectoryLocation = null;
            String userDirectory = "/mnt/" + userName + "Documents";
            String destinationDirectoryLocation = "/glue/users/" + userName;
            switch (jobType) {
                case "PYTHON":
                    sourceDirectoryLocation = userDirectory + "/.default_python_env";
                    break;
                case "R":
                    sourceDirectoryLocation = userDirectory + "/R";
                    break;
                default:
                    log.error("Invalid Script Type");
                    return;
            }
            ProcessBuilder pb = new ProcessBuilder("rsync", "-rlug", sourceDirectoryLocation, destinationDirectoryLocation);
            if (proccessExecute(pb)) {
                isSuccess = true;
            }
        } catch (Exception e) {
            isSuccess = false;
        }
    }

    private boolean proccessExecute(ProcessBuilder pb) {
        Process p=null;
        try {
            p = pb.start();
            OutputHandler out = new OutputHandler(p.getInputStream(), "UTF-8");
            OutputHandler err = new OutputHandler(p.getErrorStream(), "UTF-8");
            int status = p.waitFor();
            out.join();
            err.join();
            if (status != 0) {
                log.error("ERROR");
                log.error(err.getText());
                return false;
            }
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }finally {
            p.destroy();
        }
        return true;
    }
}
