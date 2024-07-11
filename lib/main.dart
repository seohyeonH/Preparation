import 'package:bada/Screens/Membership.dart';
import 'package:bada/Screens/SetLanguage.dart';
import 'package:flutter/material.dart';
import 'Screens/HomeScreen.dart';
import 'Screens/SelectCountry.dart';
import 'Screens/SplashScreen.dart';

void main() {
  runApp(App());
}

class App extends StatelessWidget {
  const App({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white, // Set the default background color to white
      ),
      initialRoute: '/',
      routes: {
        '/': (context) => SplashScreen(),
        '/select_country': (context) => SetACountry(),
        '/select_country_dropdown': (context) => SearchableDropdown(),
        '/select_language':(context) => SetLanguage(),
        '/home': (context) => HomeScreen(),
      },
    );
  }
}