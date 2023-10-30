import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { nativeFetch } from 'react-native-native-fetch';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();
  const [fetching, setFetching] = React.useState(false);

  const handleFetchPress = React.useCallback(() => {
    setFetching(true);
    nativeFetch('https://httpbin.org/post', {
      method: 'POST',
      body: JSON.stringify({ arg1: 'Hello', arg2: 'Alauddin' }),
    })
      .then((res) => {
        console.log('response', res);
        setResult(res);
      })
      .catch((error) => {
        console.log('ERROR', error);
      })
      .finally(() => {
        setFetching(false);
      });
  }, []);

  return (
    <View style={styles.container}>
      <Button title="Fetch" onPress={handleFetchPress} />
      {fetching && <Text>Fetching...</Text>}
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
