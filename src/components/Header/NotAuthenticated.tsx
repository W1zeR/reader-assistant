import Link from "next/link";

const NotAuthenticated = () => {
  return (
    <>
      <Link
        href="/signin"
        className="px-7 py-3 text-base font-medium text-dark hover:opacity-70 dark:text-white md:block"
      >
        Войти
      </Link>
      <Link
        href="/signup"
        className="ease-in-up shadow-btn hover:shadow-btn-hover rounded-sm bg-primary px-8 py-3 text-base font-medium
        text-white transition duration-300 hover:bg-opacity-90 md:block md:px-9 lg:px-6 xl:px-9"
      >
        Зарегистрироваться
      </Link>
    </>
  );
}

export default NotAuthenticated;
