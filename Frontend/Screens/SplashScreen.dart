import 'package:flutter/material.dart';
import 'SelectCountry.dart';
import 'dart:async';

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();
    Timer(Duration(seconds: 3), () {
      Navigator.pushReplacementNamed(context, '/select_country');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Stack(
              alignment: Alignment.center,
              children: [
                Container(
                  width: 150,
                  height: 150,
                  decoration: ShapeDecoration(
                    color: Color(0xFF3C9AFB),
                    shape: CircleBorder(),
                  ),
                ),
                Container(
                  width: 80,
                  height: 80,
                  decoration: ShapeDecoration(
                    color: Colors.white,
                    shape: CircleBorder(),
                  ),
                ),
              ],
            ),
            SizedBox(height: 16), // Add some space between the donut and the text
            Text(
              'Ba;Da',
              style: TextStyle(
                color: Colors.black,
                fontSize: 40,
                fontFamily: 'Inter',
                fontWeight: FontWeight.w400,
                height: 1.2, // Adjust height for proper text alignment
                letterSpacing: -0.24,
              ),
            ),
            SizedBox(height: 8), // Add some space between the texts
            Text(
              '어쩌구 하는 저쩌구',
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Colors.black,
                fontSize: 11,
                fontFamily: 'Inter',
                fontWeight: FontWeight.w400,
                height: 1.2, // Adjust height for proper text alignment
                letterSpacing: -0.24,
              ),
            ),
          ],
        ),
      ),
    );
  }
}