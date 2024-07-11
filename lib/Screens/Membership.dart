import 'package:flutter/material.dart';
import 'SelectCountry.dart';

class Membership extends StatelessWidget {
  final Membership id;

  Membership(this.id);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Home')),
      body: Center(
        child: Text('Welcome'),
      ),
    );
  }
}