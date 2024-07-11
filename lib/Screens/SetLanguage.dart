import 'package:bada/Screens/Login.dart';
import 'package:bada/Screens/SignUp.dart';
import 'package:flutter/material.dart';

class Language {
  final String language;
  Language(this.language);
}

class SelectCountry extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: SetLanguage(),
        ),
      ),
    );
  }
}

class SetLanguage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        SizedBox(
          width: 336,
          height: 26,
          child: Text(
            'Set Up a Language.',
            textAlign: TextAlign.center,
            style: TextStyle(
              color: Colors.black,
              fontSize: 32,
              fontFamily: 'ITF Devanagari',
              fontWeight: FontWeight.w600,
              height: 0.03,
              letterSpacing: 0.96,
            ),
          ),
        ),
        SizedBox(
          width: 134,
          child: Text(
            'Set the language you\'re comfortable with.',
            style: TextStyle(
              color: Colors.black.withOpacity(0.7),
              fontSize: 14,
              fontFamily: 'Inter',
              fontWeight: FontWeight.w400,
              height: 1,
            ),
          ),
        ),
        SizedBox(height: 20),
        Container(
          width: 353,
          height: 55,
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 18),
          decoration: ShapeDecoration(
            color: Colors.white,
            shape: RoundedRectangleBorder(
              side: BorderSide(color: Colors.black12),
              borderRadius: BorderRadius.circular(10),
            ),
          ),
          child: Material(
            child: LanguageDropdown(),
          ),
        ),
      ],
    );
  }
}

class LanguageDropdown extends StatefulWidget {
  @override
  _LanguageDropdownState createState() => _LanguageDropdownState();
}

class _LanguageDropdownState extends State<LanguageDropdown> {
  final List<Language> _languages = [
    Language("English"),//미국
    Language("中国人"),//중국
    Language("Tiếng Việt"),//베트남
    Language("แบบไทย"),//태국
  ];

  late Language _selectedLanguage;

  @override
  void initState() {
    super.initState();
    _selectedLanguage = _languages[0]; // Default selection
  }

  @override
  Widget build(BuildContext context) {
    return DropdownButtonFormField<Language>(
      value: _selectedLanguage,
      decoration: InputDecoration(
        border: InputBorder.none,
      ),
      icon: Icon(Icons.arrow_drop_down),
      items: _languages.map((Language language) {
        return DropdownMenuItem<Language>(
          value: language,
          child: Text(language.language),
        );
      }).toList(),
      onChanged: (Language? newValue) {
        setState(() {
          _selectedLanguage = newValue!;
        });
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => SignUp()),
        );
      },
    );
  }
}
