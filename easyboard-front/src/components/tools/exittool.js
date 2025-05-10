import { Tldraw, TLEventMapHandler, TLUiToolbarItem } from '@tldraw/tldraw'
import { useNavigate } from 'react-router-dom'

const CustomReturnTool = () => {
  const navigate = useNavigate()

  return (
    <button
      className="tlui-toolbar__button"
      onClick={() => navigate('/dashboard')}
      title="Вернуться в комнату"
    >
      <div className="tlui-toolbar__button__icon">
        <svg width="24" height="24" viewBox="0 0 24 24">
          <path
            fill="currentColor"
            d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"
          />
        </svg>
      </div>
    </button>
  )
}