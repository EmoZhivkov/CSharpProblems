function print_error(elemId, hintMsg) {
    document.getElementById(elemId).innerHTML = hintMsg;
}

function validate_form() {
    var is_correct = true;

    var username = document.user_form.username.value;
    var regex = /^[a-zA-Zа-яА-Я\s]{3,10}$/;
    if (regex.test(username) === false) {
        print_error("username_err", "The user name should be between 3 and 10 symbols.");
        is_correct = false;
    } else {
        print_error("username_err", "");
    }

    var name = document.user_form.name.value;
    var regex = /^[a-zA-Zа-яА-Я\s]{1,50}$/;
    if (regex.test(name) === false) {
        print_error("name_err", "The name should be between 1 and 50 symbols.");
        is_correct = false;
    } else {
        print_error("name_err", "");
    }

    var email = document.user_form.email.value;
    var regex = /\S+@\S+\.\S+/;
    if (regex.test(email) === false) {
        print_error("email_err", "Please enter a valid email format.");
        is_correct = false;
    } else {
        print_error("email_err", "");
    }

    var password = document.user_form.password.value;
    var regex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{6,10}$/;
    if (regex.test(password) === false) {
        print_error("password_err", "The password should be between 6 and 10 symbols and should contain uppercase and lowecase letters, and digits.");
        is_correct = false;
    } else {
        print_error("password_err", "");
    }

    var post_code = document.user_form.post_code.value;
    var regex = /^([*]{0}|[0-9]{5}-[0-9]{4})$/;
    if (regex.test(post_code) === false) {
        print_error("post_code_err", "The post code should be in the following format: 11111-1111 (only digits).");
        is_correct = false;
    } else {
        print_error("post_code_err", "");
    }


    return is_correct;
}