import { useFormik } from "formik";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent } from "@/components/ui/card";
import { Plus, Image as ImageIcon, Lock, Globe } from "lucide-react";
import { useCreateBoardMutation } from "@/api/board/boardApi";
import { useState } from "react";

export default function CreateBoardDialog() {
  const [createBoard, { isLoading, error }] = useCreateBoardMutation();
  const [open, setOpen] = useState(false);
  const [previewUrl, setPreviewUrl] = useState(null);

  const formik = useFormik({
    initialValues: {
      title: "",
      description: "",
      isPublic: true,
    },
    onSubmit: async (values) => {
      try {
        await createBoard({
          title: values.title,
          description: values.description,
          isPublic: values.isPublic,
        }).unwrap();
        formik.resetForm();
        setPreviewUrl(null);
        setOpen(false);
      } catch (err) {
        console.error("Ошибка при создании доски:", err);
      }
    },
  });

  const handleImageChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      formik.setFieldValue("imageFile", file);
      setPreviewUrl(URL.createObjectURL(file));
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Card className="cursor-pointer hover:shadow-lg transition-shadow flex items-center justify-center h-40">
          <CardContent className="flex flex-col items-center justify-center p-4 text-center">
            <Plus className="w-8 h-8 mb-2 text-gray-500" />
            <span className="text-sm text-gray-700">Новая доска</span>
          </CardContent>
        </Card>
      </DialogTrigger>

      <DialogContent className="sm:max-w-lg bg-white rounded-xl shadow-xl p-6">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold mb-4">Создать новую доску</DialogTitle>
        </DialogHeader>
        <form onSubmit={formik.handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="title" className="block text-sm font-medium mb-1">Имя*</label>
            <Input
              required
              name="title"
              value={formik.values.title}
              onChange={formik.handleChange}
              placeholder="Введите имя доски"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Описание (необязательно)</label>
            <Textarea
              name="description"
              value={formik.values.description}
              onChange={formik.handleChange}
              placeholder="Краткое описание доски"
              rows={3}
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Тип доски</label>
            <div className="flex gap-2">
              <button
                type="button"
                className={`flex items-center gap-2 px-4 py-2 rounded-md border transition-colors ${
                  formik.values.isPublic
                    ? "bg-blue-50 border-blue-200 text-blue-600"
                    : "bg-gray-50 border-gray-200 text-gray-600 hover:bg-gray-100"
                }`}
                onClick={() => formik.setFieldValue("isPublic", true)}
              >
                <Globe className="w-4 h-4" />
                Публичная
              </button>
              <button
                type="button"
                className={`flex items-center gap-2 px-4 py-2 rounded-md border transition-colors ${
                  !formik.values.isPublic
                    ? "bg-purple-50 border-purple-200 text-purple-600"
                    : "bg-gray-50 border-gray-200 text-gray-600 hover:bg-gray-100"
                }`}
                onClick={() => formik.setFieldValue("isPublic", false)}
              >
                <Lock className="w-4 h-4" />
                Приватная
              </button>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Изображение (необязательно)</label>
            <div className="flex items-center gap-4">
              <label
                htmlFor="file-upload"
                className="flex items-center gap-2 px-4 py-2 bg-gray-100 hover:bg-gray-200 border border-gray-300 rounded-md cursor-pointer transition-colors text-sm"
              >
                <ImageIcon className="w-4 h-4" />
                Загрузить изображение
              </label>
              <input
                id="file-upload"
                name="imageFile"
                type="file"
                accept="image/*"
                onChange={handleImageChange}
                className="hidden"
              />
              {formik.values.imageFile && (
                <p className="text-sm text-gray-600">{formik.values.imageFile.name}</p>
              )}
            </div>
            {previewUrl && (
              <img
                src={previewUrl}
                alt="Предпросмотр"
                className="mt-2 w-full max-h-40 object-cover rounded-md border border-gray-200"
              />
            )}
          </div>

          <DialogFooter className="flex justify-end space-x-2 pt-4">
            <Button 
              type="button" 
              variant="outline" 
              onClick={() => {
                setOpen(false);
                formik.resetForm();
                setPreviewUrl(null);
              }}
            >
              Отмена
            </Button>
            <Button type="submit" disabled={isLoading}>
              {isLoading ? "Создание..." : "Создать"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}