import 'package:bada/Screens/SetLanguage.dart';
import 'package:flutter/material.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';

class Country {
  final String name;
  final String imageUrl;

  Country({required this.name, required this.imageUrl});
}

class SelectCountry extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: SetACountry(),
        ),
      ),
    );
  }
}

class SetACountry extends StatelessWidget {
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
            'Set a Country',
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
            'Select your country.',
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
          child:Material(
            child: SearchableDropdown(),
    )
        ),
      ],
    );
  }
}

class SearchableDropdown extends StatefulWidget {
  @override
  _SearchableDropdownState createState() => _SearchableDropdownState();
}

class _SearchableDropdownState extends State<SearchableDropdown> {
  final List<Country> _countries = [
    Country(name: 'Korea', imageUrl: 'data:image/png;base64,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'),
    Country(name: 'China', imageUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARMAAAC3CAMAAAAGjUrGAAAAkFBMVEXuHCX//wDtACb70A7uFSXuACXuDib/+wD+8wT71A3vKSP82wv83QvwSSH97Qb/+QD5uhL3pRb84QryZx75wBHyYx7xVh/vJyL4sRT95gbzch3vMSPwQyHzexzxTiD3qxX6yBD1ixr2nxf2lxj1kRnyXR/+8AbzfRzyZh74tBPybx35vRL4rhT6yg/0hBvwOyLH6MmPAAAEp0lEQVR4nO3c6VriMBgFYPKRhlKkLAJaCrLrqCD3f3eT7ihb0oGBxPP+mHGeEZ/2tF+2plYqAAAAAPDLCX7rI7g7NBwglJRI/2qyJ2SS8JacZBbcazG69bHcC3pm65CE4zMXmWQcl7HNoM5YgEwyYsgSDWSSo3qSSVeW0K2P5cqUOxHupTcKq/bJ6nuFj1S/k1Ys97oIr3lQN0btlVoliNFOJJv5i8WDFHLXSnXAPTcNpDfeEpHFkfAX5itlIgcmsiHpyj+aNucRoQljKmVAvenshSiUmWwtj6RC8vJ3VG6UpFyow5hneSY8ajjViidBrmv74CS67rKFUL/yYtiyemgiURBlolQ82ScmlmfCm3HvqjWr40f/YQeaJ0OOUdlza9rXuCSlw9i8ZD2Qv82W32y5Y/g2HZp2y2bSaIVRKIKGfUtCoUU2gSk5DqM2cz0h6LHbsqWIqJtlsih3o0ShupVH+WPeLOmNeJhPdB/KnVK+9mbNemRROqzk8wl6Sj/+YU3pPBSZLEtcaKJxYPQSLe3jRSTyrPadvvaC3vy89Bwje512o/rTzm3C2N7/VhuzU6GIcWvn00aGwnmPaZqdLoiwOe5sguI+MzCUCo21Egm259oILmR9Rc30a7QqWbLnujEKH84FUVirXXfxKL83pEp/1psY2fXwZLVEQe2P4lWP1qTaFDW4xj7xoVXrfCCM1SvqJ1hjpi8YCLE5H8lSo70k14I1JpqdScRv6pwkVW1YsqZRcCqST0erreR2PNkQ9H48kqFuJVgRiUQftcOJPHjmNw5lkVc/FEnHyMHopdDHgUhqJk5aLoemBxsTI4eil8IPNijT39uayK7nUOkwZs1Ccxl0ZOHAmiXEEpwj057PEsVjSYzxDP9g8ehnIsqs5N4hau/ksNidFfa1rzr5+p+5R1T0Om6TaFlk0tYe2nulCu7uiH6eQS+a8VEzX4DXLh7xJod6VznK/6uYA6ZPNIWTF5PiZtniZ8kebGxB8VC60zUI87siX8B+Vr5RRPIQSP6savyF413naP8Lke4Rn+zO+MhLFrCVX84R/fXk/XMazyV9VzZQvskrKfQctxz972efLWAPVAth92mz9G70nJpe5SlU98datIpqSm2nefz920YRidl7LfiAHVmGFnyjtVmW5724PzI6kgqtj54CjxawdV64oK8kE53NpPeIWieWoWnkv+tslk3nkoa/NslHX6dOQDhzjdEGyblkrVtqTnBX+JnD1zi7qGkKvGiTbc/sG+WCZPf9KRtragbM5I74oihI9qgIev4yu3guxxtkJUMmD+wvCxWTQhB7qIOm4wea67wqZ7etF++XpUdDd/Ndhd9bEYnQ2E3TVxA9//DnTwFjG2SSyR+AWLFOfyEiXcZtvFj92wq0iD/ZolqweEIqMZ69rBOp2/L+379x8idjteryBQO3ys7WpumKo0GJxa9nv0Zba8fodxJizKrLLTm1371x5xu+cqKCoaH+I2V7pQ0IbRia1x9kh1z6Fz5Yi2bIZA8SAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgO/+Ah8/K99SlXUuAAAAAElFTkSuQmCC'),
    Country(name: 'Japan', imageUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARMAAAC3CAMAAAAGjUrGAAAAeFBMVEX////+AAD/ubn/9fX/5OT/srL/xcX/vLz/2tr/qKj/4eH+lJT/6Oj/+Pj+b2/+Li7/rKz+ODj+YWH/8PD+HBz/1NT+UVH/zMz+f3/+VVX+S0v+Wlr+ExP+i4v+Rkb+JCT+o6P+QED/hIT+PT3+dXX+lpb+Kyv+aGgfaNvJAAADdUlEQVR4nO3dgVLiMBDGcQNIEUFAqEUBFe/U93/Duw7jCZNyNG2yO1v/vyfofkObNFnSqysAAAAAAAAAAAAAAAAAAFCajvNimGXZsMjHU+2LUdfP5+vHlTu2elzP8772hWkpdr/cOfvbXPvy5GWfZ/P4ctf7STfSaH0xkIPfI+1LFZI91Uyk9DTUvlwB2SwgkdIs077kxIqPwERKH4X2ZSc02TRIpLSZaF96Ku8NEym9a198EoPQB8mp2UC7gPjmrRIpzbVLiO2udSR/ZyvaRUTVX0SIxLlFh96DxlESKY21S4llFC0S5zoy2Y8ZSUdCiXfjHHTg9hlEjsQ58xOV/jZ6Jlvro0+cQfjUQruodmJM1Xx32mW1cZMkEudutAtrLvaQ883u4NNkAameD+3Smtoli8S5nXZxzaS7c0o2757npJk8a5fXRJY0EucsLufHn8Ce2moXGC7V1OSbvUlK8kic0y4xVPsl6cusLVq/CGTyol1kmKFAJM7Z2l+/F8nkXrvMEEuRSJxbahca4FUoE0vDcdpp/bcn7ULr6wtF4pydjreeWCZ2XnrSrMJWsbMyKxaJnfl9/G2u86xsgMk9TpzraRdb061gJrfaxdYkM7E/sDK9b9fMF2amXWw9U8FIrMzaJqKZ2OgmjtuXdImNvqVCNBMbvfipN3ZO2XjjuRbN5Fq73FrS7+wcs7F4L5uJjaU2MvHxPPEx7vhk9ru+2Nj3ykUzsfF/dd53fLwXV2D9xMc6m4/1WJ/kur2N6YnswGNj2LliH7AK+8U+uQeKlW1AudYt5wz9NfBBKJIH7UIDSHQMl2wsPB7Q91iB/lgffdQV6Lf38b+MCgKZaJcYLH17+at2ieGSZ6JdYAOpX3rsvOoc2SeNZK9dXiNp+5Vs9Cd5OK+gAuda+Dj/pALn5FTgPKUKnLvlS/DXQPPns3GOXxXOe6zAuaAVYv5SOvErKU1inSO0MrNlftl0HyWSvZFGrZrqfknlf9baRcTWvrvaRsd0kEnL7yB06FFypE2fm5W+tWCDpv0GDx2Yu541bDIqb43tgQa7Xl0O4cSqg89WT28fkMizyS2LBmp/923dkbebeoaXV+A+f8pP5EixO78It1h3/bl61rKYv21O53KzzdsP/t7oP9PlOC+yLCvy8bJbb3kAAAAAAAAAAAAAAAAAADT3BzugNrnOETdrAAAAAElFTkSuQmCC'),
    Country(name: 'UA', imageUrl: 'data:image/png;base64,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'),
    Country(name: 'Tailand', imageUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARMAAAC3CAMAAAAGjUrGAAAAG1BMVEUtKkr09filGTGtM0aiACXGhpD3/P////8AADNGVaUjAAABFUlEQVR4nO3cuRECAQzAQHMP0H/F5NYMKQS7JagAzck2B5smpUlpUpqUJqVJaVKalCalSWlSmpQmpUlpUpqUJqVJaVKalCalSWlSmpQmpUlpUnOxzc02T7Z5sGlSmpQmpUlpUpqUJqVJaVKalCalSWlSmpQmpUlpUpqUJqVJaVKalCalSWlSmpQmNS+2ebMNAAAAAAAAAAAAAAAAAAAA8N2v5wB/yNei/E9Kk9KkNClNSpPSpDQpTUqT0qQ0KU1Kk9KkNClNSpPSpDQpTUqT0qQ0KU1Kk9KkNKl5ss3NNhfbHGyalCalSWlSmpQmpUlpUpqUJqVJaVKalCalSWlSmpQmpUlpUpqUJqVJaVKalCalSc3J9gFtjftfhfbsgAAAAABJRU5ErkJggg=='),
    Country(name: 'Uzbekistan', imageUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAT4AAACfCAMAAABX0UX9AAAAhFBMVEX///8etToAmbXOESbKAAB+zYgQszLXAA7XACQYtzoAlrN4u80AlbIAkrAAkbD7/v7de4HNABvbbXSez9yp1eDX7PHx+fuUy9mHxdTO5+1ftMjC4elpuMvh8fUzpL3p9fhPrsQqoru12uTkdXrWAADjc4DWABmr3bG52ePE4ul+w9OLydenwknIAAAEvElEQVR4nO2cC5OiOBRGs+z2zO41Cd3NQ1BBYJ7i//9/C7RKcNoQuWNNi9+p6qpOSVJ6KslNbqJiQR6YCC0E9E0H+lhAHwvoY3GNPpItN30794azPqmpSMKfP4PcreHSWiTHVj46jvpUGWRCCL9aOXZVlQ6KMhhUpCKcRy920qfKdeNOxImUrvY8sVH9s1JnoTaL4VbPwp+DPpJhK0+E5PqJaenHwvfXx2KxjRv50XEA51HsizjezCBkjeujctvK81fKvVW18oUIThXIa0b++iSfqOnM23wO3W9Un8z9zt6Vn1bEwuhdOvVF0DcgA+Fv9ZXv9EMypo/ybuD65VUjjfaVrne9rzLzisjQlxVeVMxg7I73vq7vifzKz9r0VWmsVRr5ZBab4SxnsXQZ0aeizl5yxbz3UNj1yaSzF81inroFI71PTBq6j4NVXxMhW3YYupew6qO40zeLGHkbbPpo09mLMfNdxKZPVp2+ag7bgxth06e63ZqYtjctra+S/eW7wTp4r4m7NOykshrUomQ4gebBPLq0Rd9hvyZcmpG6ELrPUEmlRKH6HIHSUWBkqJReZsbTd4xNX/G23XXpJ1WaiTTdH1qiIE1Flq4PI5RW6VrEu/S4TSvSXSx2aT0DfzZ9qzd9ToF31zzZD8guQxid0gxy47cZqmORinZSrecwfH9T7yNvuMBR/mDO1E0Mr/vFt2r2gukslkO20FFeMfcFWWEsr2njF5ERS0jUYWboy6qNU7MfHuvCxT3yUt0Ein6FQ4smXBhzW1EovTEeX2iZux46fWhs+nTmvu6jw9/7xfZ/uvz0/WLddbwdEVXIGFzEumx+C70x9F3EnrBCxmUEu75lpy9F97uEPdt81a73ERk566g7fdkslri3YOykLe387d8bvsQozmPZ4nDOe4gev+7cymE9OcwA0FlxdXasO5M7maO3DMr372iQSnaa+m2GLGMtjaJqavRF0uvESFA1O5LUqHzHjPY+eveOS76vM79OThuvpK7EPjntT+p6L6p6f0xYFcnCz07FtnIk6uRBblh5h72bGT+oW9KsT8XC7zJUx3K+Hez2qDs0SeSg8u53fYQ/icv1yMNxb+UZHVDtGwG90LOEFek2YdU/fpaw0g+RsDqhvLczt6V3msBUlAbCuC+6jxPRRwcqRBKbN9L8MDV8nVW+Y5zvNi+7y0LrzWGAlkulNn3OiRqzeR9sqc4VLft281qrpdFcU3k1+4TVAKlkUQdhGBz8yTagGu14vySszFhDw+JZ5fvlqu91EL7XcQa+VcQC+lhAHwvoY9Hq+9Pv4Z5ZiC9fP4OJfP0inl/+BhN5eRav/4DJvAoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACcgd8yYPCKX9Jg8PIsvn3/F0zk+zfx9Ok/MJFPT42+v8BEoI8F9LGAPhbQxwL6WEAfC+hjAX0soI8F9LGAPhbQxwL6WEAfC+hjAX0soI8F9LGAPhbQxwL6WEAfC+hjAX0soI8F9LGAPhbQxwL6WEAfC+hjAX0soI8F9LGAPhbQxwL6WEAfC+hjAX0soI8F9LGAPhbQxwL6WEAfC+hj0ekDk3kSP57AZH78D0ltMLq8oojuAAAAAElFTkSuQmCC'),
  ];

  late Country _selectedCountry;

  @override
  void initState() {
    super.initState();
    _selectedCountry = _countries[0]; // Default selection
  }

  @override
  Widget build(BuildContext context) {
    return TypeAheadFormField<Country>(
      textFieldConfiguration: TextFieldConfiguration(
        decoration: InputDecoration(
          prefixIcon: Padding(
            padding: const EdgeInsets.symmetric(vertical: 0),
            child: Icon(Icons.search, color: Colors.black26),
          ),
          hintText: 'Search',
          hintStyle: TextStyle(
            color: Colors.black.withOpacity(0.5),
            fontSize: 16,
            fontFamily: 'Inter',
            fontWeight: FontWeight.w400,
            height: 0.9,
          ),
          contentPadding: EdgeInsets.symmetric(vertical: 12, horizontal: 15),
        ),
      ),
      suggestionsCallback: (pattern) {
        return _countries.where((country) =>
            country.name.toLowerCase().contains(pattern.toLowerCase()));
      },
      itemBuilder: (context, Country suggestion) {
        return ListTile(
          leading: Image.network(suggestion.imageUrl, width: 30, height: 30),
          title: Text(suggestion.name),
        );
      },
      onSuggestionSelected: (Country suggestion) {
        setState(() {
          _selectedCountry = suggestion;
        });
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => SetLanguage(),
          ),
        );
      },
      transitionBuilder: (context, suggestionsBox, controller) {
        return suggestionsBox;
      },
      noItemsFoundBuilder: (context) => Container(
        height: 100,
        child: Center(
          child: Text('No countries found.'),
        ),
      ),
    );
  }
}
