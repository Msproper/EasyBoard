
import { categories } from '@/pages/dashboard/DashboardPage';

const Sidebar = ({ selected, onSelect, className }) => 
{
    return (
      <aside className={className}>
        <ul className="space-y-2">
          {categories.map((item, idx) => (
            <li
              key={idx}
              onClick={() => onSelect(item.title)}
              className={`cursor-pointer px-4 py-2 rounded hover:bg-blue-200 ${selected === item.title ? "bg-blue-100" : ""}`}
            >
            <div className="flex items-center gap-2">
              {item.icon}
              {item.text}
            </div>
            </li>
          ))}
        </ul>
      </aside>
    );
  };

  export default Sidebar