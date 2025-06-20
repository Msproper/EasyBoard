import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogFooter,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog"
import { AlertTriangle, UserCheck } from 'lucide-react';

export const AlertDialog = function({open, setOpen, handleSubmit}){
    return(
    <Dialog open={open} onOpenChange={setOpen}>
    <DialogContent className="sm:max-w-md bg-white rounded-xl">
      <DialogHeader>
        <DialogTitle className="text-xl font-semibold">Вход как гость</DialogTitle>
        <DialogDescription className="text-gray-600">
          Вы сможете создавать временные доски, но они будут удалены после выхода.
        </DialogDescription>
      </DialogHeader>
      <div className="grid gap-4 py-4">
        <div className="flex items-center gap-3 text-yellow-600 bg-yellow-50 p-3 rounded-lg">
          <AlertTriangle className="w-5 h-5" />
          <span className="text-sm">Все данные будут удалены после выхода</span>
        </div>
      </div>
      <DialogFooter className="flex gap-3">
        <Button 
          variant="outline" 
          onClick={() => setOpen(false)}
          className="border-gray-300 hover:bg-gray-50"
        >
          Отмена
        </Button>
        <Button 
          onClick={handleSubmit}
          className="bg-gradient-to-r from-purple-500 to-blue-500 text-white hover:from-purple-600 hover:to-blue-600"
        >
          <UserCheck className="w-4 h-4 mr-2" />
          Продолжить как гость
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
  );
}