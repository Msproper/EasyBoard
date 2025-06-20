import { unsetBoard } from '@/api/board/boardSlice'
import { Home} from 'lucide-react'
import { useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'

export function DashboardButton() {
	const dispatch = useDispatch()
	const navigate = useNavigate()
	const handleClick = () => {
		dispatch(unsetBoard())
		navigate('/dashboard', {replace:true})
	}
	
	return (
		<button
		onClick={handleClick}
		className='tlui-toolbar__button pl-3 pr-3'
		title="Вернуться в дашборд"
		>
			<div className="tlui-toolbar__button__icon">
				<Home />
			</div>
		</button>
	)
}