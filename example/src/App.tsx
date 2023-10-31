import * as React from 'react';

import { StyleSheet, View, Text, Button, TextInput } from 'react-native';
import { nativeFetch } from 'react-native-native-fetch';

export default function App() {
  const [url, setUrl] = React.useState('');
  const [result, setResult] = React.useState<string | undefined>();
  const [fetching, setFetching] = React.useState(false);

  const handleFetchPress = React.useCallback(() => {
    setFetching(true);
    nativeFetch(url, {
      method: 'POST',
      contentType: 'JSON',
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
  }, [url]);

  return (
    <View style={styles.container}>
      <TextInput
        placeholder="Enter the URL"
        value={url}
        onChangeText={setUrl}
      />
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
