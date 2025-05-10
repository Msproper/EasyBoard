import { Bell, Cat } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem, DropdownMenuSeparator } from "@/components/ui/dropdown-menu";
import { useLogoutUserMutation } from "@/api/auth/authApi";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useContext } from 'react';
import { AppContext } from "@/utils/context";
import { MenuButton } from "../Utils/MenuButton";


export const Header = () => {
  const { isSidebarOpen, setSidebarOpen } = useContext(AppContext);
  const [logoutApi] = useLogoutUserMutation()
  const navigate = useNavigate()
  const user = useSelector((state) => state.auth.user)

  const handlerButtonLogout =async ()=>{
    try{
      await logoutApi().unwrap()
      navigate("/login", {replace:true})
    } catch (e){
      console.log(e)
    }
  }

  return (

    <header className="flex bg-gradient-to-br from-green-200 via-green-100 to-blue-200 items-center justify-between px-6 py-4 shadow">
      <div className="flex items-center gap-4">
        <MenuButton className="cursor-pointer" isOpen={isSidebarOpen} onClick={()=>setSidebarOpen(!isSidebarOpen)} />
      </div>
      <div className="flex items-center gap-4">
        <Bell className="cursor-pointer" />
        <Cat className="cursor-pointer animate-bounce" />
        { user && <DropdownMenu>
          <DropdownMenuTrigger className="flex items-center gap-2">
            <Avatar>
              <AvatarImage src="https://github.com/shadcn.png" />
              <AvatarFallback>MR</AvatarFallback>
            </Avatar>
            <span className="font-medium">{user.username}</span>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="bg-white" align="end">
            <DropdownMenuItem>Настройки</DropdownMenuItem>
            <DropdownMenuItem>Данные</DropdownMenuItem>
            <DropdownMenuSeparator className="bg-black"/>
            <DropdownMenuItem className="text-red-500" onClick={handlerButtonLogout}>Выйти</DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>}
      </div>
    </header>
  );
};

