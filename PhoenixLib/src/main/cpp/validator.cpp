#include <jni.h>
#include <string>
#include <unistd.h>
#include <android/log.h>

extern "C" JNIEXPORT int JNICALL
Java_com_faddy_phoenixlib_SdkInternal_systemSetup(JNIEnv *env, jobject /* this */) {

    char path[64] = {0};
    char application_id[64] = {0};
    pid_t pid = getpid();
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");

    if (cmdline) {
        fread(application_id, sizeof(application_id), 1, cmdline);
        fclose(cmdline);
    }
    char appName[64] = "com.faddy.vpnsdk";
    if (strcmp(application_id, appName) != 0) {
        kill(pid, SIGKILL);
    }
    return 1;
}