import { CheckCircleIcon, BanIcon, EyeOffIcon } from "lucide-react";

export const getAccessStatus = (board) => {
    if (board.isBlocked ) {
    return {
        text: "Заблокировано",
        icon: <BanIcon className="w-4 h-4 text-red-500" />,
        color: "text-red-500",
    };
    }
    if (board.isAccess) {
    return {
        text: "Доступ открыт",
        icon: <CheckCircleIcon className="w-4 h-4 text-green-500" />,
        color: "text-green-500",
    };
    }
    return {
    text: "Нет доступа",
    icon: <EyeOffIcon className="w-4 h-4 text-gray-400" />,
    color: "text-gray-400",
    };
};
