import 'package:flutter/material.dart';
import 'package:bada/Service/validate.dart';

class SignUp extends StatefulWidget {
  const SignUp({Key? key}) : super(key: key);

  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  final FocusNode _firstNameFocus = FocusNode();
  final FocusNode _secondNameFocus = FocusNode();
  final FocusNode _IDFocus = FocusNode();
  final FocusNode _passwordFocus = FocusNode();
  final FocusNode _confirmPasswordFocus = FocusNode();
  final FocusNode _birthFocus = FocusNode();

  final GlobalKey<FormState> formKey = GlobalKey<FormState>();
  final TextEditingController _passwordController = TextEditingController();
  String? _selectedGender;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Create New Account')),
      body: Form(
        key: formKey,
        child: SingleChildScrollView(
          child: Column(
            children: [
              _showFirstNameInput(),
              _showSecondNameInput(),
              _showIDInput(),
              _showPasswordInput(),
              _showConfirmPasswordInput(),
              _showGenderInput(),
              _showBirthInput(),
              _showOkBtn()
            ],
          ),
        ),
      ),
    );
  }

  Widget _showFirstNameInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        focusNode: _firstNameFocus,
        keyboardType: TextInputType.name,
        decoration: InputDecoration(
          border: OutlineInputBorder(),
          labelText: 'First Name',
          hintText: 'Please write your First Name.',
        ),
        validator: (value) {
          if (value == null || value.isEmpty) {
            return 'First Name is required';
          }
          return null;
        },
      ),
    );
  }

  Widget _showSecondNameInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        focusNode: _secondNameFocus,
        keyboardType: TextInputType.name,
        decoration: InputDecoration(
          border: OutlineInputBorder(),
          labelText: 'Second Name',
          hintText: 'Please write your Second Name.',
        ),
        validator: (value) {
          if (value == null || value.isEmpty) {
            return 'Second Name is required';
          }
          return null;
        },
      ),
    );
  }

  Widget _showIDInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        focusNode: _IDFocus,
        keyboardType: TextInputType.text,
        decoration: InputDecoration(
          border: OutlineInputBorder(),
          labelText: 'ID',
          hintText: 'Please write your ID.',
        ),
        validator: (value) {
          if (value == null || value.isEmpty) {
            return 'ID is required';
          }
          return null;
        },
      ),
    );
  }

  Widget _showPasswordInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        controller: _passwordController,
        focusNode: _passwordFocus,
        keyboardType: TextInputType.visiblePassword,
        obscureText: true,
        decoration: InputDecoration(
          border: OutlineInputBorder(),
          labelText: 'Password',
          hintText: 'Must contain 8 characters.',
        ),
        validator: (value) => CheckValidate().validatePassword(_passwordFocus, value!),
      ),
    );
  }

  Widget _showConfirmPasswordInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        focusNode: _confirmPasswordFocus,
        keyboardType: TextInputType.visiblePassword,
        obscureText: true,
        decoration: InputDecoration(
          border: OutlineInputBorder(),
          labelText: 'Confirm Password',
          hintText: 'Check your password.',
        ),
        validator: (value) {
          if (value != _passwordController.text) {
            return 'Passwords do not match';
          }
          return null;
        },
      ),
    );
  }

  Widget _showGenderInput() {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            'Select your gender:',
            style: TextStyle(fontSize: 20),
          ),
          ListTile(
            title: const Text('Male'),
            leading: Radio<String>(
              value: 'Male',
              groupValue: _selectedGender,
              onChanged: (String? value) {
                setState(() {
                  _selectedGender = value!;
                });
              },
            ),
          ),
          ListTile(
            title: const Text('Female'),
            leading: Radio<String>(
              value: 'Female',
              groupValue: _selectedGender,
              onChanged: (String? value) {
                setState(() {
                  _selectedGender = value!;
                });
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _showBirthInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        focusNode: _birthFocus,
        keyboardType: TextInputType.datetime,
        decoration: InputDecoration(
          border: OutlineInputBorder(),
          labelText: 'Date of Birth',
          hintText: 'YYYY-MM-DD',
        ),
        validator: (value) => CheckValidate().validateBirthDate(_birthFocus, value!),
      ),
    );
  }

  Widget _showOkBtn() {
    return Padding(
      padding: EdgeInsets.only(top: 20),
      child: MaterialButton(
        height: 50,
        color: Colors.blue,
        child: Text('Submit', style: TextStyle(color: Colors.white)),
        onPressed: () {
          if (formKey.currentState!.validate()) {
            // Process data
          }
        },
      ),
    );
  }
}