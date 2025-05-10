import { useCreateInviteMutation } from "@/api/invites/inviteApi";
import { Card, CardContent } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { inviteStatusTypes } from "@/const/inviteStatusTypes";
import { notificationTypesClasses } from "@/const/notificationTypesClasses";
import { useContext } from "react";
import { AppContext } from "@/utils/context";


const BoardsGrid = ({boards}) => {
  const navigate = useNavigate()
  const {showAlert} = useContext(AppContext)
  const [createInvite, {isLoading, isError}] = useCreateInviteMutation()

  const handleClick = async (id) => {
    try {
    const data = await createInvite(id).unwrap()
    
    if (data.status == inviteStatusTypes.ACCEPTED){
      showAlert(notificationTypesClasses.SUCCESS, "Успешно", 5000)
      navigate("/boards/"+data.uuid, {replace:true})
    }
    else if (data.status == inviteStatusTypes.PENDING){
      showAlert(notificationTypesClasses.SUCCESS, "Приглашение отправлено, ожидайте")
    } } catch(e){
      console.log(e)
      showAlert(notificationTypesClasses.ERROR, e.data.message)
    }
  }

  return (<>
        {boards && boards.map((board) => (
          <Card key={board.id} className="hover:shadow-lg transition-shadow" onClick={()=>handleClick(board.id)}>
            <CardContent className="p-4">{board.title}</CardContent>
          </Card>
        ))}
      </>
    );
  };
export default BoardsGrid