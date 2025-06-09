
import { useState } from 'react';
import { Plus, Key } from 'lucide-react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";

const GroupsPage = ()=> {
  const [code, setCode] = useState("");

  return (
    <div className="min-h-screen bg-white flex items-center justify-center p-6">
      <div className="max-w-3xl w-full space-y-8">
        <h1 className="text-3xl font-bold text-center">Добро пожаловать в группу</h1>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Подключиться по коду */}
          <div className="p-6 bg-gradient-to-br from-green-100 to-blue-100 rounded-2xl shadow-md">
            <div className="flex items-center gap-2 mb-4">
              <Key className="text-blue-500" />
              <h2 className="text-xl font-semibold">Подключиться по коду</h2>
            </div>
            <Input 
              placeholder="Введите код группы..." 
              value={code}
              onChange={(e) => setCode(e.target.value)}
            />
            <Button className="mt-4 w-full">Подключиться</Button>
          </div>

          {/* Создать группу */}
          <div className="p-6 bg-gradient-to-br from-green-100 to-blue-100 rounded-2xl shadow-md">
            <div className="flex items-center gap-2 mb-4">
              <Plus className="text-blue-500" />
              <h2 className="text-xl font-semibold">Создать группу</h2>
            </div>
            <Dialog>
              <DialogTrigger asChild>
                <Button className="w-full bg-white">Создать новую группу</Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Создание новой группы</DialogTitle>
                </DialogHeader>
                <form className="space-y-4">
                  <Input placeholder="Название группы" />
                  <Textarea placeholder="Описание группы (необязательно)" />
                  <select className="w-full p-2 border rounded-md">
                    <option value="public">Публичная</option>
                    <option value="code">По коду</option>
                  </select>
                  <Button type="submit" className="w-full">Создать</Button>
                </form>
              </DialogContent>
            </Dialog>
          </div>
        </div>
      </div>
    </div>
  );
}

export default GroupsPage
