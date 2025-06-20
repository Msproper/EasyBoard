// Floating Action Button + Menu + Modal (share only)
import { useState } from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Copy, LinkIcon, QrCode } from "lucide-react";
import QRCode from "react-qr-code";
import { useGetAccessInviteQuery } from "@/api/invites/inviteApi";
import { useDispatch } from "react-redux";
import { showNotification } from "@/api/notification/notificationSlice";
import { notificationTypesClasses } from "@/const/notificationTypesClasses";

export function InviteControls({ boardId }) {
  const [open, setOpen] = useState(false);
  const [modal, setModal] = useState(null);

  return (
    <div className="fixed bottom-6 right-6 z-50">
      <div className="relative">
        <Button
          className="rounded-full bg-blue-400 text-white w-14 h-14 p-0 shadow-lg mb-10"
          onClick={() => setOpen((prev) => !prev)}
        >
          ≡
        </Button>

        {open && (
          <div className="absolute bottom-16 right-0 flex flex-col gap-2 bg-white p-2 rounded-xl shadow-md mb-10">
            <Button onClick={() => {}} variant="ghost" className="justify-start">
              🛠️ Роли
            </Button>
            <Button onClick={() => {}} variant="ghost" className="justify-start">
              🙋 Пригласить
            </Button>
            <Button
              onClick={() => {
                setModal("share");
                setOpen(false);
              }}
              variant="ghost"
              className="justify-start"
            >
              🔗 Поделиться
            </Button>
          </div>
        )}
      </div>

      <ShareModal open={modal === "share"} onOpenChange={() => setModal(null)} boardId={boardId} />
    </div>
  );
}

function ShareModal({ open, onOpenChange, boardId }) {
  const { data, isLoading } = useGetAccessInviteQuery(boardId);
  const link = data ? `${window.location.origin}/invite/${data.uuid}` : "";
  const code = data?.code || "";

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="bg-white w-full max-w-md mx-auto p-4 rounded-lg shadow-lg">
        <DialogHeader>
          <DialogTitle className="text-lg font-semibold">Поделиться доской</DialogTitle>
        </DialogHeader>

        <div className="flex flex-col gap-4">
          <div className="mx-auto">
            {isLoading ? (
              <div className="w-28 h-28 bg-gray-100 animate-pulse rounded" />
            ) : (
              <QRCode value={link} size={128} className="mx-auto" />
            )}
          </div>

          <CopyField
            icon={<LinkIcon className="w-8 h-8 text-gray-600" />}
            label="Ссылка"
            value={link}
          />
          <CopyField
            icon={<QrCode className="w-8 h-8 text-gray-600" />}
            label="Код"
            value={code}
            isCode
          />
        </div>
      </DialogContent>
    </Dialog>
  );
}function CopyField({ icon, label, value, isCode = false }) {
  const dispatch = useDispatch()

  const handleCopy = async () => {
    await navigator.clipboard.writeText(value);
    dispatch(showNotification({ 
      type: notificationTypesClasses.SUCCESS, 
      message: "Скопировано!"
    }));
  };

  return (
    <div className="flex flex-col gap-2 w-full">
      <div className="flex items-center gap-2">
        {icon}
        <span className="text-s font-medium text-gray-500">{label}</span>
      </div>

      <div className="flex gap-2 w-full items-stretch">
        <div className="relative flex-1 min-w-0">
          <div
            title={value}
            className={` max-h-40 overflow-y-auto
 w-full bg-gray-50 rounded p-2 text-sm break-words ${
              isCode ? "text-blue-600 font-medium font-mono" : "text-gray-700"
            } ${!value && "text-gray-400"}`}
          >
            {value || (isCode ? "—" : "—")}
          </div>
          {value && (
            <div className="absolute inset-y-0 right-0 flex items-center pr-2 bg-gradient-to-l from-gray-50 to-transparent w-8">
              <Button
                size="sm"
                variant="ghost"
                className="h-6 w-6 p-1 hover:bg-gray-200"
                onClick={handleCopy}
              >
                <Copy className="w-3 h-3" />
              </Button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
