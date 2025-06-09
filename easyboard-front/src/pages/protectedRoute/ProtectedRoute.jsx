
import { Navigate, Outlet } from 'react-router-dom';
import { useUpdateUserQuery } from '@/api/auth/authApi';
import { Spinner } from '@/components/Utils/Spinner';

export const ProtectedRoute = () => {
  const { isLoading, isSuccess } = useUpdateUserQuery();
  
  if (isLoading) {
    return <Spinner />; // Показываем загрузку во время проверки
  }
  
  if (!isSuccess) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }
  
  return <Outlet />;
};