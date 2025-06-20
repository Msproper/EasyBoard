import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import { useSelector } from "react-redux"
import { useDispatch } from "react-redux"
import { useNavigate } from "react-router-dom"
import { motion } from "framer-motion"
import first_img from "./8640447.jpg"
import second_img from "./5204985.jpg"
import third_img from "./20945597.jpg"
import { useAnonymousSignUpMutation } from "@/api/auth/authApi"
import { AlertDialog } from "../login/AlertDialog"
import { showNotification } from "@/api/notification/notificationSlice"
import { notificationTypesClasses } from "@/const/notificationTypesClasses"


export default function WelcomePage() {
  const user = useSelector((state)=> state.auth.user);
  const [open, setOpen] = useState(false)
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [anonymousSignUp] = useAnonymousSignUpMutation()
  const sections = [
    {
      title: "Присоединяйтесь к нам",
      text: "Создавайте доски, работайте в команде и достигайте целей вместе.",
      photo: first_img, 
      button: (
        <div className="flex flex-col sm:flex-row gap-4 mt-4">
          <Button
            className="bg-gradient-to-r from-green-400 to-blue-500 text-white px-6 py-3 text-lg"
            onClick={() => navigate("/login", { replace: true })}
          >
            Зарегистрироваться / Войти
          </Button>
          <Button
            variant="outline"
            className="text-black border-black px-6 py-3 text-lg hover:bg-black/5"
            onClick={() => setOpen(true)}
          >
            Продолжить как гость
          </Button>
        </div>
      ),
    },
    {
      title: "Визуальная совместная работа",
      text: "Работайте в реальном времени с другими, создавайте идеи и визуализируйте решения.",
      photo: second_img
    },
    {
      title: "Безопасность данных",
      text: "Ваши данные защищены. Используем современные методы шифрования и токены доступа.",
      photo: third_img
    },
  ]

  const handleSubmit = async () => {
    try {
      await anonymousSignUp().unwrap();
      navigate("/dashboard", { replace: true });
    } catch (err) {
    dispatch(showNotification({type:notificationTypesClasses.ERROR, message:err.body}))
    } 
  };

    useEffect(() => {
    if (user) {
      navigate("/dashboard", { replace: true });
    }
  }, [user, navigate]);


  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-green-100 via-blue-50 to-blue-200 overflow-hidden">
      <div className="container mx-auto py-16 px-6 space-y-24">
        {sections.map((section, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 40 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            viewport={{ once: true }}
            className={`flex flex-col-reverse bg-white p-4 rounded-xl md:flex-row items-center justify-between gap-10 ${
              index % 2 === 1 ? "md:flex-row-reverse" : ""
            }`}
          >
            <div className="md:w-1/2 text-center md:text-left space-y-4">
              <h2 className="text-4xl font-bold text-gray-800">{section.title}</h2>
              <p className="text-gray-600 text-2xl">{section.text}</p>
              {section.button}
            </div>
            <div className="md:w-1/2">
              <img src={section.photo} className="ml-8 h-128 w-full rounded-xl bg-gradient-to-br from-green-300 to-blue-300 shadow-xl"></img>
            </div>
          </motion.div>
        ))}
      </div>

      <AlertDialog open={open} setOpen={setOpen} handleSubmit={handleSubmit}></AlertDialog>
    </div>
  )
}

