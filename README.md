# react-native-native-fetch

Native HTTP request api

## Installation

```sh
npm install react-native-native-fetch
```

### Android

- Add following permissions into `android/src/main/AndroidManifest.xml`

```android
<uses-permission android:name="android.permission.INTERNET" />
```

- Also set `usesCleartextTraffic` to `true` to enable `http` (non `https`) requests as below.

```android
<application
    android:usesCleartextTraffic="true"
/>
```

## Usage

```ts
import { nativeFetch } from 'react-native-native-fetch';

// ...

const payload = { arg1: 'Hello', arg2: 'World' };

const result = await nativeFetch('http://some-url.com', {
  body: JSON.stringify(payload),
  contentType: 'JSON',
  method: 'POST',
}: INativeFetchOptions): Promise<any>;

// types
interface INativeFetchOptions {
  body?: string;
  contentType?: 'JSON' | 'PLAIN' | 'MULTIPART';
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
}
```

## TODO

- [x] Android support
- [ ] iOS support

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Created by Alauddin Ansari (alauddinx27@gmail.com)
