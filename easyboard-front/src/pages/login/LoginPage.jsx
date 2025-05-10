import { useEffect, useRef } from 'react';
import { useSignUpMutation, useSignInMutation, useUpdateUserQuery } from '../../api/auth/authApi';
import { motion } from "framer-motion";
import { FaUser, FaEnvelope, FaLock } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import '../../App.css';
import { loginValidationSchema } from '@/utils/utils';
import { useState } from 'react';

function Login() {
  const [isLogin, setIsLogin] = useState(false);
  const {isLoading, error} = useUpdateUserQuery();
  const [signUp, { error: signUpError }] = useSignUpMutation();
  const [signIn, { error: signInError }] = useSignInMutation();
  const navigate = useNavigate();

  useEffect(() => {
    if (!error) {
      navigate('/dashboard', { replace: true });
    }
  }, [ navigate]);

  const handleSubmit = async (values, { setSubmitting }) => {
    try {
      if (isLogin) {
        await signIn({ 
          username: values.username, 
          password: values.password 
        }).unwrap();
        navigate("/dashboard", { replace: true });
      } else {
        await signUp(values).unwrap();
        navigate("/dashboard", { replace: true });
      }
    } catch (err) {
      console.error('Ошибка:', err);
    } finally {
      setSubmitting(false);
    }
  };

  // Объединяем ошибки от бэкенда
  const backendError = signUpError || signInError;

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 via-green-50 to-blue-100 overflow-hidden relative">
      <div className="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md z-10">
        <h2 className="text-2xl font-bold text-center text-blue-700 mb-6">
          {isLogin ? 'Вход' : 'Регистрация'}
        </h2>

        <Formik
          initialValues={{ 
            username: '', 
            email: '', 
            password: '',
            isLogin: isLogin
          }}
          validationSchema={loginValidationSchema}
          onSubmit={handleSubmit}
          enableReinitialize
        >
          {({ isSubmitting }) => (
            <>
              {backendError && (
                <div className="mb-4 p-3 bg-red-100 border-l-4 border-red-500 text-red-700">
                  <p>{backendError.data?.message || 'Произошла ошибка'}</p>
                </div>
              )}

              <Form className="space-y-5">
                <div>
                  <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                    Имя пользователя
                  </label>
                  <div className="mt-1 relative">
                    <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                      <FaUser />
                    </span>
                    <Field
                      name="username"
                      type="text"
                      className="pl-10 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-200"
                    />
                  </div>
                  <ErrorMessage name="username" component="div" className="text-red-500 text-sm mt-1" />
                </div>

                {!isLogin && (
                  <div>
                    <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                      Email
                    </label>
                    <div className="mt-1 relative">
                      <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                        <FaEnvelope />
                      </span>
                      <Field
                        name="email"
                        type="email"
                        className="pl-10 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-200"
                      />
                    </div>
                    <ErrorMessage name="email" component="div" className="text-red-500 text-sm mt-1" />
                  </div>
                )}

                <div>
                  <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                    Пароль
                  </label>
                  <div className="mt-1 relative">
                    <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-400">
                      <FaLock />
                    </span>
                    <Field
                      name="password"
                      type="password"
                      className="pl-10 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-200"
                    />
                  </div>
                  <ErrorMessage name="password" component="div" className="text-red-500 text-sm mt-1" />
                </div>

                <motion.button
                  whileHover={{ scale: 1.03 }}
                  whileTap={{ scale: 0.97 }}
                  type="submit"
                  disabled={isSubmitting}
                  className="w-full bg-blue-400 hover:bg-blue-500 text-white font-semibold py-2 px-4 rounded-lg transition-all duration-300 disabled:opacity-50"
                >
                  {isSubmitting ? 'Обработка...' : isLogin ? 'Войти' : 'Зарегистрироваться'}
                </motion.button>
              </Form>
            </>
          )}
        </Formik>

        <p className="text-center text-sm text-gray-500 mt-4">
          {isLogin ? 'Нет аккаунта?' : 'Уже есть аккаунт?'}{' '}
          <button
            type="button"
            onClick={() => setIsLogin(!isLogin)}
            className="text-blue-600 hover:underline"
          >
            {isLogin ? 'Зарегистрироваться' : 'Войти'}
          </button>
        </p>
      </div>
    </div>
  );
}


export default Login;