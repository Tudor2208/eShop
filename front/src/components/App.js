import { Toaster } from 'sonner';

const handleData = (data) => {
  console.log(data);
}

function App() {
  return (
    <>
      <Toaster richColors position="bottom-center" expand={true}/>
    </>
  );
}

export default App;
