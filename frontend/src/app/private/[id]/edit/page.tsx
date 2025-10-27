import EditQuoteForm from "@/components/Forms/EditQuoteForm";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Помощник читателя | Редактировать цитату",
  description: "Это страница редактирования цитаты"
};

const EditQuote = ({ params }: { params: { id: string } }) => {
  const id = params.id;

  return (
    <>
      <section className="relative z-10 overflow-hidden pb-16 pt-36 md:pb-20 lg:pb-28 lg:pt-[180px]">
        <div className="container">
          <div className="-mx-4 flex flex-nowrap">
            <div className="w-full px-4">
              <div className="shadow-three mx-auto max-w-[500px] rounded bg-white px-6 py-10 dark:bg-dark sm:p-[60px]">
                <h3 className="mb-3 text-center text-2xl font-bold text-black dark:text-white sm:text-3xl">
                  Изменить цитату
                </h3>
                <div className="mb-8 flex items-center justify-center" />
                <EditQuoteForm id={id} />
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default EditQuote;
