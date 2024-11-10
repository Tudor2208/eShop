import { Toaster } from 'sonner';
import Header from './Header';
import CategoriesRibbon from './CategoriesRibbon';
import Navbar from './Navbar';

const handleData = (data) => {
  console.log(data);
}

function App() {
  return (
    <>
      <Toaster richColors position="bottom-center" expand={true}/>
      <Navbar />
    </>
  );
}

export default App;
