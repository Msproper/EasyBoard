import { Tldraw, track, useEditor } from 'tldraw'
import 'tldraw/tldraw.css'
import { useYjsStore } from '@/utils/useYjsStore'
import { Navigate} from 'react-router-dom'
import {
	DefaultToolbar,DefaultToolbarContent
  } from 'tldraw'
import 'tldraw/tldraw.css'
import { DashboardButton } from '@/components/tools/DashboardButton'
import { InviteControls } from '@/components/Utils/test'
import { useSelector } from 'react-redux'
import { useRef } from 'react'
const HOST_URL =  import.meta.env.MODE === 'development'
? 'ws://localhost:1234'
: 'ws://localhost/socket.io'



function CustomToolbar() {
	return (
		<DefaultToolbar>
			<DashboardButton />
			<DefaultToolbarContent />
		</DefaultToolbar>
	)
}

export default function board() {
	const roomId = useSelector((store) => store.board.boardUuid )
	const boardId = useSelector((store) => store.board.boardId )
	if (roomId == null || roomId == 0) return <Navigate to="/dashboard" replace/>
	//const {roomId} = useParams()
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
			>
				{boardId && <InviteControls boardId={boardId}></InviteControls>}
			</Tldraw>
		</div>
	)
}

const NameEditor = track(() => {
	const editor = useEditor()
	const { color, name } = editor.user.getUserPreferences()
	const colorInputRef = useRef(null)
  
	const handleColorCircleClick = () => {
	  colorInputRef.current?.click()
	}
  
	return (
	  <div style={{ pointerEvents: 'all'}} className="flex items-center gap-3 p-2 bg-white rounded-lg shadow-md border border-gray-200">
		{/* Круглый выбор цвета */}
		<div 
		  className="relative w-8 h-8 rounded-full border-2 border-white shadow-md transition-transform duration-200 hover:scale-110 cursor-pointer"
		  style={{ backgroundColor: color }}
		  onClick={handleColorCircleClick}
		>
		  <input
			type="color"
			ref={colorInputRef}
			value={color}
			onChange={(e) => {
			  editor.user.updateUserPreferences({
				color: e.currentTarget.value,
			  })
			}}
			className="absolute inset-0 w-full h-full opacity-0"
		  />
		</div>
		<input
		  value={name}
		  onChange={(e) => {
			editor.user.updateUserPreferences({
			  name: e.currentTarget.value,
			})
		  }}
		  className="px-3 py-1 text-sm bg-gray-50 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all"
		  placeholder="Ваше имя"
		/>
	  </div>
	)
  })

