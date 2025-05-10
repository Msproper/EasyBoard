import { Tldraw, track, useEditor } from 'tldraw'
import 'tldraw/tldraw.css'
import { useYjsStore } from '@/utils/useYjsStore'
import { useNavigate, useParams } from 'react-router-dom'
import {
	DefaultToolbar,DefaultToolbarContent
  } from 'tldraw'
import 'tldraw/tldraw.css'
import { Home} from 'lucide-react'

const HOST_URL = 'ws://localhost/socket.io'

function DashboardButton() {
	const navigate = useNavigate()
	const handleClick = () => {
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

function CustomToolbar() {
	return (
		<DefaultToolbar>
			<DashboardButton />
			<DefaultToolbarContent />
		</DefaultToolbar>
	)
}

export default function board() {
	const {roomId} = useParams()
	const store = useYjsStore({
		roomId: roomId,
		hostUrl: HOST_URL,
	})

	return (
		<div className="absolute inset-0">
			<Tldraw
				autoFocus
				store={store}
				components={{
					SharePanel: NameEditor,
					Toolbar: CustomToolbar,
				}}
				overrides={{
					toolbar(_, toolbar) {
					  return [
						{
						  id: 'dashboard',
						  type: 'item',
						  readonly: true,
						  toolItem: {
							id: 'dashboard',
							icon: 'chevron-left',
							label: 'Dashboard',
							onSelect: () => navigate('/dashboard'),
						  },
						},
						...toolbar,
					  ]
					},
				  }}
			></Tldraw>
		</div>
	)
}

const NameEditor = track(() => {
	const editor = useEditor()

	const { color, name } = editor.user.getUserPreferences()

	return (
		<div style={{ pointerEvents: 'all', display: 'flex' }}>
			<input
				type="color"
				value={color}
				onChange={(e) => {
					editor.user.updateUserPreferences({
						color: e.currentTarget.value,
					})
				}}
			/>
			<input
				value={name}
				onChange={(e) => {
					editor.user.updateUserPreferences({
						name: e.currentTarget.value,
					})
				}}
			/>
		</div>
	)
})

