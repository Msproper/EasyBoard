import { useState } from "react"
import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogFooter,
  DialogTitle,
} from "@/components/ui/dialog"
import { motion } from "framer-motion"
import { useNavigate } from "react-router-dom"

export default function WelcomePage() {
  const [open, setOpen] = useState(false)
  const navigate = useNavigate()

  return (
    <div className="relative min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 via-green-50 to-blue-100 overflow-hidden">
      {/* Фоновые фигуры */}
      <div className="absolute inset-0 pointer-events-none">
        {[...Array(30)].map((_, i) => (
          <motion.div
            key={i}
            className="absolute w-16 h-16 rounded-full opacity-20 bg-gradient-to-br from-purple-400 to-orange-300"
            style={{
              top: `${Math.random() * 100}%`,
              left: `${Math.random() * 100}%`,
              transform: `rotate(${Math.random() * 360}deg)`,
            }}
            animate={{ y: [-20, 20] }}
            transition={{
              duration: 6 + Math.random() * 4,
              repeat: Infinity,
              repeatType: "reverse",
            }}
          />
        ))}
      </div>

      {/* Контент */}
      <div className="z-10 text-center space-y-6 bg-white backdrop-blur-lg p-10 rounded-2xl shadow-xl">
        <h1 className="text-4xl font-bold text-black">Добро пожаловать!</h1>
        <p className="text-black text-lg">Выберите, как хотите продолжить:</p>
        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <Button
            variant="default"
            className="px-6 py-3 text-lg font-semibold bg-gradient-to-r from-green-300 via-green-150 to-blue-400 text-white hover:scale-102"
            onClick={()=>{navigate("/login", { replace: true })}}>
            Зарегистрироваться / Войти
          </Button>
          <Button
            variant="outline"
            className="px-6 py-3 text-lg font-semibold border-black text-black hover:bg-black/4 hover:scale-102"
            onClick={() => setOpen(true)}
          >
            Продолжить как гость
          </Button>
        </div>
      </div>

      {/* Модальное окно */}
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-md bg-white">
          <DialogHeader>
            <DialogTitle>Предупреждение</DialogTitle>
            <p className="text-sm text-gray-600">
              Продолжая как гость, вы можете потерять данные после выхода.
            </p>
          </DialogHeader>
          <DialogFooter className="flex gap-4">
            <Button variant="ghost" onClick={() => setOpen(false)}>
              Согласен
            </Button>
            <Button
              className="bg-gradient-to-r from-orange-400 to-purple-600 text-white"
              onClick={() => {
                setOpen(false)
                // логика перехода к регистрации
              }}
            >
              Зарегистрироваться
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}
