
import { ref, uploadBytesResumable, getDownloadURL } from "firebase/storage";
import { storage } from "./firebase"; // Make sure Firebase is properly configured
import { v4 as uuidv4 } from "uuid"; // For generating unique file names

const uploadImage = async (img) => {
  if (!img) return null;

  const uniqueFileName = `${uuidv4()}_${img.name}`; // Generate a unique name for the image
  const imageRef = ref(storage, `images/${uniqueFileName}`); // Create a reference in Firebase Storage

  try {
    // Upload the image to Firebase
    const uploadTask = await uploadBytesResumable(imageRef, img);

    // Get the download URL once the upload is complete
    const imgUrl = await getDownloadURL(uploadTask.ref);
    return imgUrl;
  } catch (error) {
    console.error("Error during image upload:", error);
    return null;
  }
};

export default uploadImage;
