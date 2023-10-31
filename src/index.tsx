import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-native-fetch' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const NativeFetchApi = NativeModules.NativeFetch
  ? NativeModules.NativeFetch
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

interface INativeFetchOptions {
  body?: string;
  contentType?: 'JSON' | 'PLAIN' | 'MULTIPART';
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
}

const defaultOptions: INativeFetchOptions = {
  contentType: 'JSON',
  method: 'GET',
};

export function nativeFetch(
  url: string,
  options?: INativeFetchOptions
): Promise<string> {
  return NativeFetchApi.fetch(url, Object.assign({}, defaultOptions, options));
}
