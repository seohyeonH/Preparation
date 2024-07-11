import 'package:flutter/material.dart';
import 'package:bada/Service/validate.dart';

class SignUp extends StatefulWidget {
  const SignUp({Key? key}) : super(key: key);

  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  final FocusNode _passwordFocus = FocusNode();
  final FocusNode _confirmPasswordFocus = FocusNode();
  final FocusNode _birthFocus = FocusNode();

  final GlobalKey<FormState> formKey = GlobalKey<FormState>();

  String? _selectedGender;
  final TextEditingController _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Create New Account')),
      body: Form(
        key: formKey,
        child: SingleChildScrollView(
          child: Column(
            children: [
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

  Widget _showPasswordInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        controller: _passwordController,
        focusNode: _passwordFocus,
        keyboardType: TextInputType.visiblePassword,
        obscureText: true,
        decoration: _textFormDecoration(
            '비밀번호', '특수문자, 대소문자, 숫자 포함 8자 이상 15자 이내로 입력하세요.'),
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
        decoration: _textFormDecoration('비밀번호 확인', '비밀번호를 다시 입력해주세요'),
        validator: (value) {
          if (value != _passwordController.text) {
            return '비밀번호가 일치하지 않습니다';
          }
          return null;
        },
      ),
    );
  }

  Widget _showGenderInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: DropdownButtonFormField<String>(
        decoration: _textFormDecoration('성별', '성별을 선택해주세요'),
        value: _selectedGender,
        items: ['남성', '여성', '기타'].map((String value) {
          return DropdownMenuItem<String>(
            value: value,
            child: Text(value),
          );
        }).toList(),
        onChanged: (newValue) {
          setState(() {
            _selectedGender = newValue;
          });
        },
        validator: (value) => value == null ? '성별을 선택해주세요' : null,
      ),
    );
  }

  Widget _showBirthInput() {
    return Padding(
      padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: TextFormField(
        focusNode: _birthFocus,
        keyboardType: TextInputType.datetime,
        decoration: _textFormDecoration('생년월일', 'YYYY-MM-DD 형식으로 입력하세요'),
        validator: (value) => CheckValidate().validateBirthDate(_birthFocus, value!),
      ),
    );
  }

  InputDecoration _textFormDecoration(String hintText, String helperText) {
    return InputDecoration(
      contentPadding: EdgeInsets.fromLTRB(0, 16, 0, 0),
      hintText: hintText,
      helperText: helperText,
    );
  }

  Widget _showOkBtn() {
    return Padding(
      padding: EdgeInsets.only(top: 20),
      child: MaterialButton(
        height: 50,
        color: Colors.blue,
        child: Text('확인', style: TextStyle(color: Colors.white)),
        onPressed: () {
          if (formKey.currentState!.validate()) {
            // Process data
          }
        },
      ),
    );
  }
}
