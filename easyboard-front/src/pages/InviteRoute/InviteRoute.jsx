import { setBoard } from "@/api/board/boardSlice";
import { useSendAccessInviteUuidMutation } from "@/api/invites/inviteApi";
import { showNotification } from "@/api/notification/notificationSlice";
import { notificationTypesClasses } from "@/const/notificationTypesClasses";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";


export const InviteRoute = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [sendAccessInviteUuid] = useSendAccessInviteUuidMutation();
    const { inviteUuid } = useParams();
  
    useEffect(() => {
      const fetchInvite = async () => {
        try {
          const data = await sendAccessInviteUuid(inviteUuid).unwrap();
          console.log(data);
          dispatch(setBoard({ boardId: null, boardUuid: data.uuid }));
          navigate("/board", { replace: true });
        } catch (e) {
          console.error("Ошибка при активации инвайта:", e);
          dispatch(showNotification({type:notificationTypesClasses.ERROR, message:"Ошибка при подключении к доске"}))
          navigate("/dashboard", { replace: true });
        }
      };
  
      if (inviteUuid) {
        fetchInvite();
      }
    }, [inviteUuid, dispatch, navigate, sendAccessInviteUuid]);
  
    return null;
};