import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('samples.flutter.dev/q1Printer');

  String _resultValue = '.....';

  Future<void> _goPrint() async {
    String resultValue;
    try {
      final int result = await platform.invokeMethod('goPrint');
      resultValue = 'printer result $result % .';
    } on PlatformException catch (e) {
      resultValue = "Failed to print: '${e.message}'.";
    }

    setState(() {
      _resultValue = resultValue;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            ElevatedButton(
              child: const Text('test print'),
              onPressed: _goPrint,
            ),
            Text(_resultValue),
          ],
        ),
      ),
    );
  }
}
