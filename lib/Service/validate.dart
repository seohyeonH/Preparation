import 'package:flutter/material.dart';
import 'package:bada/Screens/SignUp.dart';

class CheckValidate {

  String? validateFirstName(FocusNode focusNode, String value) {
    if (value.isEmpty) {
      focusNode.requestFocus();
      return '이름 입력';
    }
    return null;
  }

  String? validateSecondName(FocusNode focusNode, String value) {
    if (value.isEmpty) {
      focusNode.requestFocus();
      return '이름 입력';
    }
    return null;
  }

  // Password validation
  String? validatePassword(FocusNode focusNode, String value) {
    if (value.isEmpty) {
      focusNode.requestFocus();
      return '비밀번호를 입력해주세요';
    }
    if (value.length < 8 || value.length > 15) {
      focusNode.requestFocus();
      return '비밀번호는 8자 이상 15자 이내여야 합니다';
    }
    RegExp regex = RegExp(r'^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$');
    if (!regex.hasMatch(value)) {
      focusNode.requestFocus();
      return '특수문자, 대소문자, 숫자 포함 8자 이상 입력하세요';
    }
    return null;
  }

  // Birth date validation
  String? validateBirthDate(FocusNode focusNode, String value) {
    if (value.isEmpty) {
      focusNode.requestFocus();
      return '생년월일을 입력해주세요';
    }
    RegExp regex = RegExp(r'^\d{4}-\d{2}-\d{2}$');
    if (!regex.hasMatch(value)) {
      focusNode.requestFocus();
      return 'YYYY-MM-DD 형식으로 입력하세요';
    }
    try {
      DateTime birthDate = DateTime.parse(value);
      if (birthDate.isAfter(DateTime.now())) {
        focusNode.requestFocus();
        return '미래 날짜는 입력할 수 없습니다';
      }
    } catch (e) {
      focusNode.requestFocus();
      return '유효한 날짜를 입력해주세요';
    }
    return null;
  }
}
