package com.faddy.singboxsdk.aidl;

import com.faddy.singboxsdk.aidl.IServiceCallback;

interface IService {
  int getStatus();
  void registerCallback(in IServiceCallback callback);
  oneway void unregisterCallback(in IServiceCallback callback);
}