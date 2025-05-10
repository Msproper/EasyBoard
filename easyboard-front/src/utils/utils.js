import * as Yup from 'yup';


export const loginValidationSchema = Yup.object().shape({
    username: Yup.string()
      .min(5, 'Имя должно содержать минимум 5 символов')
      .max(50, 'Имя не должно превышать 50 символов')
      .required('Обязательное поле'),
    email: Yup.string()
      .email('Некорректный email')
      .min(5, 'Email должен содержать минимум 5 символов')
      .max(255, 'Email не должен превышать 255 символов')
      .when('isLogin', {
        is: false,
        then: schema => schema.required('Обязательное поле')
      }),
    password: Yup.string()
      .min(2, 'Пароль должен содержать минимум 2 символа')
      .max(255, 'Пароль не должен превышать 255 символов')
      .required('Обязательное поле')
  });